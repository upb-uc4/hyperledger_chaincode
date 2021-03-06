package de.upb.cs.uc4.chaincode.contract.examresult;

import de.upb.cs.uc4.chaincode.contract.ContractBase;
import de.upb.cs.uc4.chaincode.exceptions.SerializableError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.LedgerAccessError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.ParameterError;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import de.upb.cs.uc4.chaincode.helper.HyperledgerManager;
import de.upb.cs.uc4.chaincode.model.examresult.ExamResultEntry;
import org.hyperledger.fabric.contract.annotation.Contract;
import de.upb.cs.uc4.chaincode.model.examresult.ExamResult;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Arrays;
import java.util.List;

@Contract(
        name = ExamResultContract.contractName
)

public class ExamResultContract extends ContractBase {
    private final ExamResultContractUtil cUtil = new ExamResultContractUtil();

    public final static String contractName = "UC4.ExamResult";
    public final static String transactionNameAddExamResult = "addExamResult";
    public final static String transactionNameGetExamResultEntries= "getExamResultEntries";

    /**
     * Adds an examResult.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param examResult    examResult to be added
     * @return examResult object on ledger on success, serialized error on failure
     */
    @Transaction()
    public String addExamResult(final Context ctx, String examResult) {
        String transactionName = HyperledgerManager.getTransactionName(ctx.getStub());
        final String[] args = new String[]{examResult};
        ChaincodeStub stub = ctx.getStub();

        try {
            cUtil.validateTransaction(ctx, contractName,  transactionName, args);
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        ExamResult newExamResult = GsonWrapper.fromJson(examResult, ExamResult.class);

        return cUtil.putAndGetStringState(stub, cUtil.getKey(newExamResult), GsonWrapper.toJson(newExamResult));

    }

    /**
     * Gets ExamResultEntries from the ledger.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId enrollmentId
     * @param examIds      examIds
     * @return the full list of existing ExamResultEntries matching the filter parameters or an empty list of none could be found
     */

    @Transaction()
    public String getExamResultEntries(final Context ctx, String enrollmentId, String examIds) {
        String transactionName = HyperledgerManager.getTransactionName(ctx.getStub());
        String[] args = new String[]{enrollmentId, examIds};
        ChaincodeStub stub = ctx.getStub();

        try {
            cUtil.validateTransaction(ctx, contractName,  transactionName, args);
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        List<ExamResultEntry>examResults = cUtil.getExamResultEntries(stub, enrollmentId,
                Arrays.asList(GsonWrapper.fromJson(examIds, String[].class).clone()));
        return GsonWrapper.toJson(examResults);
    }

}
