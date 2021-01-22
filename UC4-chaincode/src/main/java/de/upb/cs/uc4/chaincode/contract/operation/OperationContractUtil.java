package de.upb.cs.uc4.chaincode.contract.operation;

import de.upb.cs.uc4.chaincode.contract.ContractUtil;
import de.upb.cs.uc4.chaincode.contract.group.GroupContractUtil;
import de.upb.cs.uc4.chaincode.exceptions.serializable.LedgerAccessError;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import de.upb.cs.uc4.chaincode.model.ApprovalList;
import de.upb.cs.uc4.chaincode.model.OperationData;
import de.upb.cs.uc4.chaincode.model.OperationDataState;
import de.upb.cs.uc4.chaincode.model.TransactionInfo;
import de.upb.cs.uc4.chaincode.model.errors.DetailedError;
import de.upb.cs.uc4.chaincode.model.errors.GenericError;
import de.upb.cs.uc4.chaincode.model.errors.InvalidParameter;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class OperationContractUtil extends ContractUtil {
    private static final String HASH_DELIMITER = ":";
    private static final GroupContractUtil groupContractUtil = new GroupContractUtil();

    public OperationContractUtil() {
        keyPrefix = "operation:";
        thing = "operationData";
        identifier = "operationId";
    }

    public String getUserRejectionMessage(String message) {
        return message;
    }

    public List<OperationData> getOperations(ChaincodeStub stub, final String existingEnrollmentId, final String missingEnrollmentId, final String initiatorEnrollmentId, String state) {
        List<String> groupsForUserMissingApproval = new GroupContractUtil().getGroupNamesForUser(stub, missingEnrollmentId);

        return this.getAllStates(stub, OperationData.class).stream()
                .filter(item -> existingEnrollmentId.isEmpty() ||
                        item.getExistingApprovals().getUsers().contains(existingEnrollmentId))
                .filter(item -> missingEnrollmentId.isEmpty() ||
                        item.getMissingApprovals().getUsers().contains(missingEnrollmentId) ||
                        item.getMissingApprovals().getGroups().stream().anyMatch(groupsForUserMissingApproval::contains))
                .filter(item -> initiatorEnrollmentId.isEmpty() ||
                        item.getInitiator().equals(initiatorEnrollmentId))
                .filter(item -> state.isEmpty() ||
                        item.getState().toString().equals(state)).collect(Collectors.toList());
    }

    public static boolean covers(ApprovalList requiredApprovals, ApprovalList existingApprovals) {
        return getMissingApprovalList(requiredApprovals, existingApprovals).isEmpty();
    }

    public static ApprovalList getMissingApprovalList(ApprovalList requiredApprovals, ApprovalList existingApprovals) {
        ApprovalList missingApprovals = new ApprovalList();
        missingApprovals.setUsers(requiredApprovals.getUsers().stream().filter(user -> !existingApprovals.getUsers().contains(user)).collect(Collectors.toList()));
        missingApprovals.setGroups(requiredApprovals.getGroups().stream().filter(group -> !existingApprovals.getGroups().contains(group)).collect(Collectors.toList()));
        return missingApprovals;
    }

    public static String getDraftKey(final String contractName, final String transactionName, final String params) throws NoSuchAlgorithmException {
        String all = contractName + HASH_DELIMITER + transactionName + HASH_DELIMITER + params.replace(" ", "");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(all.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getUrlEncoder().encode(bytes));
    }

    public DetailedError getContractUnprocessableError(String contractName) {
        return getUnprocessableEntityError(new InvalidParameter()
                .name("contractName")
                .reason("The given contract \"" + contractName + "\" does not exist"));
    }

    public DetailedError getTransactionUnprocessableError(String transactionName) {
        return getUnprocessableEntityError(new InvalidParameter()
                .name("transactionName")
                .reason("The given transaction \"" + transactionName + "\" does not exist"));
    }

    public DetailedError getEmptyContractNameError() {
        return getUnprocessableEntityError(getEmptyInvalidParameter("contractName"));
    }

    public DetailedError getEmptyTransactionNameError() {
        return getUnprocessableEntityError(getEmptyInvalidParameter("transactionName"));
    }

    public OperationData getOrInitializeOperationData(Context ctx, String initiator, String contractName, String transactionName, String params) throws NoSuchAlgorithmException {
        String key = OperationContractUtil.getDraftKey(contractName, transactionName, params);
        OperationData operationData;
        String timeStamp = getTimestamp(ctx.getStub());
        try{
            operationData = getState(ctx.getStub(), key, OperationData.class);
        } catch (LedgerAccessError ledgerAccessError) {
            operationData = new OperationData()
                    .initiator(initiator)
                    .operationId(key)
                    .initiatedTimestamp(timeStamp)
                    .transactionInfo(new TransactionInfo().contractName(contractName).transactionName(transactionName).parameters(params))
                    .state(OperationDataState.PENDING)
                    .reason("");

        }
        return operationData;
    }
}
