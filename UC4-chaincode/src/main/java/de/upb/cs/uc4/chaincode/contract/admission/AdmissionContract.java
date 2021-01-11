package de.upb.cs.uc4.chaincode.contract.admission;

import de.upb.cs.uc4.chaincode.contract.ContractBase;
import de.upb.cs.uc4.chaincode.exceptions.SerializableError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.LedgerAccessError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.ParameterError;
import de.upb.cs.uc4.chaincode.model.Admission;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Contract(
        name = "UC4.Admission"
)
public class AdmissionContract extends ContractBase {
    private final AdmissionContractUtil cUtil = new AdmissionContractUtil();

    public final String contractName = "UC4.Admission";

    /**
     * Adds MatriculationData to the ledger.
     *
     * @param ctx           transaction context providing access to ChaincodeStub etc.
     * @param admissionJson json representation of new Admission to add.
     * @return newAdmission on success, serialized error on failure
     */
    @Transaction()
    public String addAdmission(final Context ctx, String admissionJson) {
        try {
            cUtil.checkParamsAddAdmission(ctx, admissionJson);
        } catch (ParameterError e) {
            return e.getJsonError();
        }

        ChaincodeStub stub = ctx.getStub();
        Admission newAdmission = GsonWrapper.fromJson(admissionJson, Admission.class);
        newAdmission.resetAdmissionId();

        try {
            cUtil.validateApprovals(stub, this.contractName,  "addAdmission", Collections.singletonList(admissionJson));
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        // TODO: can we create a composite key of all inputs to improve reading performance for get...forUser/Module/Course
        return cUtil.putAndGetStringState(stub, newAdmission.getAdmissionId(), GsonWrapper.toJson(newAdmission));
    }

    /**
     * Drops an existing admission from the ledger
     *
     * @param ctx         transaction context providing access to ChaincodeStub etc.
     * @param admissionId identifier of admission to drop
     * @return empty string on success, serialized error on failure
     */
    @Transaction()
    public String dropAdmission(final Context ctx, String admissionId) {
        try {
            cUtil.checkParamsDropAdmission(ctx, admissionId);
        } catch (LedgerAccessError e) {
            return e.getJsonError();
        }

        ChaincodeStub stub = ctx.getStub();
        try {
            cUtil.validateApprovals(stub, this.contractName,  "dropAdmission", Collections.singletonList(admissionId));
        } catch (SerializableError e) {
            return e.getJsonError();
        }

        // perform delete
        try {
            cUtil.delState(stub, admissionId);
        } catch (LedgerAccessError e) {
            return e.getJsonError();
        }
        // success
        return "";
    }

    /**
     * Gets AdmissionList from the ledger.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId enrollment to find admissions for
     * @return Serialized List of Matching Admissions on success, serialized error on failure
     */
    @Transaction()
    public String getAdmissions(final Context ctx, final String enrollmentId, final String courseId, final String moduleId) {
        ChaincodeStub stub = ctx.getStub();
        try {
            cUtil.validateApprovals(stub, this.contractName,  "getAdmissions", new ArrayList<String>() {{add(enrollmentId);add(courseId);add(moduleId);}});
        } catch (SerializableError e) {
            return e.getJsonError();
        }
        List<Admission> admissions = cUtil.getAdmissions(stub, enrollmentId, courseId, moduleId);
        return GsonWrapper.toJson(admissions);
    }
}