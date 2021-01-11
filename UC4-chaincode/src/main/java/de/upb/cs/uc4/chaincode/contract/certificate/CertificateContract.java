package de.upb.cs.uc4.chaincode.contract.certificate;

import de.upb.cs.uc4.chaincode.contract.ContractBase;
import de.upb.cs.uc4.chaincode.exceptions.SerializableError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.ParameterError;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.Collections;

@Contract(
        name = "UC4.Certificate"
)
public class CertificateContract extends ContractBase {

    private final CertificateContractUtil cUtil = new CertificateContractUtil();

    public final String contractName = "UC4.Certificate";

    /**
     * Adds a certificate to the ledger.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId enrollmentId for the certificate to be added
     * @param certificate  certificate to be set for the given enrollmentId
     * @return certificate on success, serialized error on failure
     */
    @Transaction()
    public String addCertificate(final Context ctx, final String enrollmentId, final String certificate) {
        try {
            cUtil.checkParamsAddCertificate(ctx, enrollmentId, certificate);
        } catch (ParameterError e) {
            return e.getJsonError();
        }

        ChaincodeStub stub = ctx.getStub();
        try {
            cUtil.validateApprovals(stub, this.contractName,  "addCertificate", new ArrayList<String>(){{add(enrollmentId);add(certificate);}});
        } catch (SerializableError e) {
            return e.getJsonError();
        }
        return cUtil.putAndGetStringState(stub, enrollmentId, certificate);
    }

    /**
     * Updates certificate on the ledger.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId enrollmentId for the certificate to be updated
     * @param certificate  certificate to be set for the given enrollmentId
     * @return certificate on success, serialized error on failure
     */
    @Transaction()
    public String updateCertificate(final Context ctx, final String enrollmentId, final String certificate) {
        try {
            cUtil.checkParamsUpdateCertificate(ctx, enrollmentId, certificate);
        } catch (ParameterError e) {
            return e.getJsonError();
        }

        ChaincodeStub stub = ctx.getStub();
        try {
            cUtil.validateApprovals(stub, this.contractName,  "updateCertificate", new ArrayList<String>(){{add(enrollmentId);add(certificate);}});
        } catch (SerializableError e) {
            return e.getJsonError();
        }
        return cUtil.putAndGetStringState(stub, enrollmentId, certificate);
    }

    /**
     * Gets certificate from the ledger.
     *
     * @param ctx          transaction context providing access to ChaincodeStub etc.
     * @param enrollmentId enrollmentId of the certificate to be returned
     * @return certificate on success, serialized error on failure
     */
    @Transaction()
    public String getCertificate(final Context ctx, final String enrollmentId) {
        try {
            cUtil.checkParamsGetCertificate(ctx, enrollmentId);
        } catch (ParameterError e) {
            return e.getJsonError();
        }

        ChaincodeStub stub = ctx.getStub();
        try {
            cUtil.validateApprovals(stub, this.contractName,  "getCertificate", Collections.singletonList(enrollmentId));
        } catch (SerializableError e) {
            return e.getJsonError();
        }
        String certificate = cUtil.getStringState(stub, enrollmentId);
        return certificate;
    }
}