package de.upb.cs.uc4.chaincode.model.admission;

import com.google.gson.annotations.SerializedName;
import de.upb.cs.uc4.chaincode.contract.admission.AdmissionContractUtil;
import de.upb.cs.uc4.chaincode.model.errors.InvalidParameter;
import io.swagger.annotations.ApiModelProperty;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.Objects;

public class ExamAdmission extends AbstractAdmission {

    public ExamAdmission () {
        type = AdmissionType.EXAM;
    }

    @SerializedName("examId")
    private String examId;

    public void resetAdmissionId() {
        this.admissionId = this.enrollmentId + DELIMITER + this.examId;
    }

    @ApiModelProperty()
    public String getExamId() {
        return this.examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
        resetAdmissionId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExamAdmission other = (ExamAdmission) o;
        return Objects.equals(this.admissionId, other.admissionId)
                && Objects.equals(this.enrollmentId, other.enrollmentId)
                && Objects.equals(this.examId, other.examId)
                && Objects.equals(this.timestamp, other.timestamp)
                && Objects.equals(this.type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.admissionId, this.enrollmentId, this.examId, this.timestamp);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Admission {\n");
        sb.append("    admissionId: ").append(toIndentedString(this.admissionId)).append("\n");
        sb.append("    enrollmentId: ").append(toIndentedString(this.enrollmentId)).append("\n");
        sb.append("    courseId: ").append(toIndentedString(this.examId)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(this.timestamp)).append("\n");
        sb.append("    type: ").append(toIndentedString(this.type)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


    @Override
    public ArrayList<InvalidParameter> getParameterErrors() {
        AdmissionContractUtil cUtil = new AdmissionContractUtil();
        ArrayList<InvalidParameter> invalidparams = new ArrayList<>();

        if (cUtil.valueUnset(this.getEnrollmentId())) {
            invalidparams.add(cUtil.getEmptyEnrollmentIdParam(cUtil.getErrorPrefix() + "."));
        }

        // TODO analogous to CourseAdmission

        return invalidparams;
    }

    public ArrayList<InvalidParameter> getSemanticErrors(ChaincodeStub stub) {
        AdmissionContractUtil cUtil = new AdmissionContractUtil();
        ArrayList<InvalidParameter> invalidParameters = new ArrayList<>();

        // TODO

        return invalidParameters;
    }
}

