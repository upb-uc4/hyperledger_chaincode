package de.upb.cs.uc4.chaincode.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class SubmissionResult {
    @SerializedName("operationId")
    private String operationId = null;
    @SerializedName("existingApprovals")
    private ApprovalList existingApprovals = null;
    @SerializedName("missingApprovals")
    private ApprovalList missingApprovals = null;

    public SubmissionResult operationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    @ApiModelProperty(value = "")
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public SubmissionResult existingApprovals(ApprovalList existingApprovals) {
        this.existingApprovals = existingApprovals;
        return this;
    }

    @ApiModelProperty(value = "")
    public ApprovalList getExistingApprovals() {
        return existingApprovals;
    }

    public void setExistingApprovals(ApprovalList existingApprovals) {
        this.existingApprovals = existingApprovals;
    }

    public SubmissionResult missingApprovals(ApprovalList missingApprovals) {
        this.missingApprovals = missingApprovals;
        return this;
    }

    @ApiModelProperty(value = "")
    public ApprovalList getMissingApprovals() {
        return missingApprovals;
    }

    public void setMissingApprovals(ApprovalList missingApprovals) {
        this.missingApprovals = missingApprovals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubmissionResult examinationRegulation = (SubmissionResult) o;
        return Objects.equals(this.existingApprovals, examinationRegulation.existingApprovals) &&
                Objects.equals(this.missingApprovals, examinationRegulation.missingApprovals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(existingApprovals, missingApprovals);
    }

}
