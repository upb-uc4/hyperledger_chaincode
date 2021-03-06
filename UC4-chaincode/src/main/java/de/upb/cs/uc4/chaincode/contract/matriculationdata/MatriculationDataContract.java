package de.upb.cs.uc4.chaincode.contract.matriculationdata;

import de.upb.cs.uc4.chaincode.contract.ContractBase;
import de.upb.cs.uc4.chaincode.exceptions.serializable.LedgerAccessError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.ParameterError;
import de.upb.cs.uc4.chaincode.exceptions.SerializableError;
import de.upb.cs.uc4.chaincode.helper.HyperledgerManager;
import de.upb.cs.uc4.chaincode.model.matriculation.MatriculationData;
import de.upb.cs.uc4.chaincode.model.matriculation.SubjectMatriculation;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Arrays;
import java.util.List;

@Contract(
        name = MatriculationDataContract.contractName
)
public class MatriculationDataContract extends ContractBase {
    private final MatriculationDataContractUtil cUtil = new MatriculationDataContractUtil();

    public final static String contractName = "UC4.MatriculationData";
    public final static String transactionNameAddMatriculationData = "addMatriculationData";
    public final static String transactionNameUpdateMatriculationData = "updateMatriculationData";
    public final static String transactionNameGetMatriculationData = "getMatriculationData";
    public final static String transactionNameAddEntriesToMatriculationData = "addEntriesToMatriculationData";

    /**
     * Adds MatriculationData to the ledger.
     *
     * @param ctx               transaction context providing access to ChaincodeStub etc.
     * @param matriculationData MatriculationData to be added
     * @return newMatriculationData on success, serialized error on failure
     */
    @Transaction()
    public String addMatriculationData(final Context ctx, String matriculationData) {
        String transactionName = HyperledgerManager.getTransactionName(ctx.getStub());
        final String[] args = new String[]{matriculationData};
        ChaincodeStub stub = ctx.getStub();

        try {
            cUtil.validateTransaction(ctx, contractName,  transactionName, args);
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        MatriculationData newMatriculationData = GsonWrapper.fromJson(matriculationData, MatriculationData.class);
        return cUtil.putAndGetStringState(stub, newMatriculationData.getEnrollmentId(), GsonWrapper.toJson(newMatriculationData));
    }

    /**
     * Updates MatriculationData on the ledger.
     *
     * @param ctx               transaction context providing access to ChaincodeStub etc.
     * @param matriculationData json-representation of the new MatriculationData to replace the old with
     * @return updatedMatriculationData on success, serialized error on failure
     */
    @Transaction()
    public String updateMatriculationData(final Context ctx, String matriculationData) {
        String transactionName = HyperledgerManager.getTransactionName(ctx.getStub());
        final String[] args = new String[]{matriculationData};
        ChaincodeStub stub = ctx.getStub();

        try {
            cUtil.validateTransaction(ctx, contractName,  transactionName, args);
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        MatriculationData newMatriculationData = GsonWrapper.fromJson(matriculationData, MatriculationData.class);
        return cUtil.putAndGetStringState(stub, newMatriculationData.getEnrollmentId(), GsonWrapper.toJson(newMatriculationData));
    }

    /**
     * Gets MatriculationData from the ledger.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId enrollmentId of the MatriculationData to be returned
     * @return Serialized MatriculationData on success, serialized error on failure
     */
    @Transaction()
    public String getMatriculationData(final Context ctx, final String enrollmentId) {
        String transactionName = HyperledgerManager.getTransactionName(ctx.getStub());
        final String[] args = new String[]{enrollmentId};
        ChaincodeStub stub = ctx.getStub();

        try {
            cUtil.validateTransaction(ctx, contractName,  transactionName, args);
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        MatriculationData matriculationData;
        try {
            matriculationData = cUtil.getState(stub, enrollmentId, MatriculationData.class);
        } catch (LedgerAccessError e) {
            return e.getJsonError();
        }
        return GsonWrapper.toJson(matriculationData);
    }

    /**
     * Adds a semester entry to a fieldOfStudy of MatriculationData on the ledger.
     *
     * @param ctx            transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId   enrollmentId to add the matriculations to
     * @param matriculations list of matriculations
     * @return Updated MatriculationData on success, serialized error on failure
     */
    @Transaction()
    public String addEntriesToMatriculationData(
            final Context ctx,
            final String enrollmentId,
            final String matriculations) {
        String transactionName = HyperledgerManager.getTransactionName(ctx.getStub());
        final String[] args = new String[]{enrollmentId, matriculations};
        ChaincodeStub stub = ctx.getStub();

        try {
            cUtil.validateTransaction(ctx, contractName,  transactionName, args);
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        List<SubjectMatriculation> matriculationStatus;
        matriculationStatus = Arrays.asList(GsonWrapper.fromJson(matriculations, SubjectMatriculation[].class));

        MatriculationData matriculationData;
        try {
            matriculationData = cUtil.getState(stub, enrollmentId, MatriculationData.class);
        } catch (LedgerAccessError e) {
            return e.getJsonError();
        }

        matriculationData.addAbsent(matriculationStatus);
        return cUtil.putAndGetStringState(stub, matriculationData.getEnrollmentId(), GsonWrapper.toJson(matriculationData));
    }
}
