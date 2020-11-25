package de.upb.cs.uc4.chaincode.model;

import com.google.gson.annotations.SerializedName;
import de.upb.cs.uc4.chaincode.util.*;
import io.swagger.annotations.ApiModelProperty;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MatriculationData
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-07-26T19:00:46.792+02:00")



public class JsonIOTestSetup {

  @SerializedName("approvalContract")
  private List<Dummy> approvalContract = null;

  public JsonIOTestSetup approvalContract(List<Dummy> approvalContract) {
    this.approvalContract = approvalContract;
    return this;
  }

  public JsonIOTestSetup addApprovalContractItem(Dummy approvalContractItem) {
    if (this.approvalContract == null) {
      this.approvalContract = new ArrayList<Dummy>();
    }
    this.approvalContract.add(approvalContractItem);
    return this;
  }

  @ApiModelProperty(value = "")
  public List<Dummy> getApprovalContract() {
    return approvalContract;
  }

  public void setApprovalContract(List<Dummy> approvalContract) {
    this.approvalContract = approvalContract;
  }

  @SerializedName("certificateContract")
  private List<Dummy> certificateContract = null;

  public JsonIOTestSetup certificateContract(List<Dummy> certificateContract) {
    this.certificateContract = certificateContract;
    return this;
  }

  public JsonIOTestSetup addCertificateContractItem(Dummy certificateContractItem) {
    if (this.certificateContract == null) {
      this.certificateContract = new ArrayList<Dummy>();
    }
    this.certificateContract.add(certificateContractItem);
    return this;
  }

  @ApiModelProperty(value = "")
  public List<Dummy> getCertificateContract() {
    return certificateContract;
  }

  public void setCertificateContract(List<Dummy> certificateContract) {
    this.certificateContract = certificateContract;
  }

  @SerializedName("examinationRegulationContract")
  private List<Dummy> examinationRegulationContract = null;

  public JsonIOTestSetup examinationRegulationContract(List<Dummy> examinationRegulationContract) {
    this.examinationRegulationContract = examinationRegulationContract;
    return this;
  }

  public JsonIOTestSetup addExaminationRegulationContractItem(Dummy examinationRegulationContractItem) {
    if (this.examinationRegulationContract == null) {
      this.examinationRegulationContract = new ArrayList<Dummy>();
    }
    this.examinationRegulationContract.add(examinationRegulationContractItem);
    return this;
  }

  @ApiModelProperty(value = "")
  public List<Dummy> getExaminationRegulationContract() {
    return examinationRegulationContract;
  }

  public void setExaminationRegulationContract(List<Dummy> examinationRegulationContract) {
    this.examinationRegulationContract = examinationRegulationContract;
  }

  @SerializedName("matriculationDataContract")
  private List<Dummy> matriculationDataContract = null;

  public JsonIOTestSetup matriculationDataContract(List<Dummy> matriculationDataContract) {
    this.matriculationDataContract = matriculationDataContract;
    return this;
  }

  public JsonIOTestSetup addMatriculationDataContractItem(Dummy matriculationDataContractItem) {
    if (this.matriculationDataContract == null) {
      this.matriculationDataContract = new ArrayList<Dummy>();
    }
    this.matriculationDataContract.add(matriculationDataContractItem);
    return this;
  }

  @ApiModelProperty(value = "")
  public List<Dummy> getMatriculationDataContract() {
    return matriculationDataContract;
  }

  public void setMatriculationDataContract(List<Dummy> matriculationDataContract) {
    this.matriculationDataContract = matriculationDataContract;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonIOTestSetup matriculationData = (JsonIOTestSetup) o;
    return Objects.equals(this.approvalContract, matriculationData.approvalContract) &&
            Objects.equals(this.certificateContract, matriculationData.certificateContract) &&
            Objects.equals(this.examinationRegulationContract, matriculationData.examinationRegulationContract) &&
            Objects.equals(this.matriculationDataContract, matriculationData.matriculationDataContract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvalContract, certificateContract, examinationRegulationContract, matriculationDataContract);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonIOTestSetup {\n");
    
    sb.append("    approvalContract: ").append(toIndentedString(approvalContract)).append("\n");
    sb.append("    certificateContract: ").append(toIndentedString(certificateContract)).append("\n");
    sb.append("    examinationRegulationContract: ").append(toIndentedString(examinationRegulationContract)).append("\n");
    sb.append("    matriculationDataContract: ").append(toIndentedString(matriculationDataContract)).append("\n");
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

  public void prepareStub(ChaincodeStub stub) {
    ContractUtil cUtil = new ApprovalContractUtil();
    List<String> setup = TestUtil.toStringList(this.approvalContract);
    for (int i=0; i<setup.size(); i+=2) {
      cUtil.putAndGetStringState(stub, setup.get(i).toString(), setup.get(i+1));
    }

    cUtil = new CertificateContractUtil();
    setup = TestUtil.toStringList(this.certificateContract);
    for (int i=0; i<setup.size(); i+=2) {
      cUtil.putAndGetStringState(stub, setup.get(i).toString(), setup.get(i+1));
    }

    cUtil = new ExaminationRegulationContractUtil();
    setup = TestUtil.toStringList(this.examinationRegulationContract);
    for (int i=0; i<setup.size(); i+=2) {
      cUtil.putAndGetStringState(stub, setup.get(i).toString(), setup.get(i+1));
    }

    cUtil = new MatriculationDataContractUtil();
    setup = TestUtil.toStringList(this.matriculationDataContract);
    for (int i=0; i<setup.size(); i+=2) {
      cUtil.putAndGetStringState(stub, setup.get(i).toString(), setup.get(i+1));
    }
  }

}
