package de.upb.cs.uc4.chaincode.contract.matriculationdata;

import com.google.gson.reflect.TypeToken;
import de.upb.cs.uc4.chaincode.contract.ContractUtil;
import de.upb.cs.uc4.chaincode.contract.examinationregulation.ExaminationRegulationContractUtil;
import de.upb.cs.uc4.chaincode.exceptions.serializable.LedgerAccessError;
import de.upb.cs.uc4.chaincode.exceptions.serializable.ParameterError;
import de.upb.cs.uc4.chaincode.exceptions.SerializableError;
import de.upb.cs.uc4.chaincode.model.ExaminationRegulation;
import de.upb.cs.uc4.chaincode.model.MatriculationData;
import de.upb.cs.uc4.chaincode.model.SubjectMatriculation;
import de.upb.cs.uc4.chaincode.model.errors.InvalidParameter;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MatriculationDataContractUtil extends ContractUtil {
    public MatriculationDataContractUtil() {
        keyPrefix = "matriculationData";
        thing = "MatriculationData";
        identifier = "enrollmentId";
    }

    public InvalidParameter getUnparsableMatriculationDataParam() {
        return super.getUnparsableParam("matriculationData");
    }

    public InvalidParameter getUnparsableMatriculationParam() {
        return super.getUnparsableParam("matriculations");
    }

    public InvalidParameter getInvalidFieldOfStudyParam(String prefix) {
        return new InvalidParameter()
                .name(prefix + "fieldOfStudy")
                .reason("Field of study must be one of the specified values");
    }

    public InvalidParameter getDuplicateFieldOfStudyParam(String prefix, int index) {
        return new InvalidParameter()
                .name(prefix + "[" + index + "].fieldOfStudy")
                .reason("Each field of study must only appear in one matriculationStatus");
    }

    public InvalidParameter getDuplicateSemesterParam(String prefix, int index) {
        return new InvalidParameter()
                .name(prefix + "[" + index + "]")
                .reason("Each semester must only appear once in matriculationStatus.semesters");
    }

    public InvalidParameter getInvalidSemesterParam(String prefix, int index) {
        return new InvalidParameter()
                .name(prefix + "[" + index + "]")
                .reason("Semester must be the following format \"(WS\\d{4}/\\d{2}|SS\\d{4})\", e.g. \"WS2020/21\"");
    }

    /**
     * Returns a list of errors describing everything wrong with the given matriculationData
     *
     * @param matriculationData matriculationData to return errors for
     * @param prefix            prefix used for error information
     * @return a list of all errors found for the given matriculationData
     */
    public ArrayList<InvalidParameter> getErrorForMatriculationData(
            ChaincodeStub stub,
            MatriculationData matriculationData,
            String prefix) {

        if (!prefix.isEmpty())
            prefix += ".";

        ArrayList<InvalidParameter> invalidparams = new ArrayList<>();

        if (valueUnset(matriculationData.getEnrollmentId())) {
            invalidparams.add(getEmptyEnrollmentIdParam(prefix));
        }

        invalidparams.addAll(getErrorForSubjectMatriculationList(
                stub,
                matriculationData.getMatriculationStatus(),
                prefix + "matriculationStatus"));
        return invalidparams;
    }

    public ArrayList<InvalidParameter> getErrorForSubjectMatriculationList(
            ChaincodeStub stub,
            List<SubjectMatriculation> matriculationStatus,
            String prefix) {
        ExaminationRegulationContractUtil eUtil = new ExaminationRegulationContractUtil();

        ArrayList<InvalidParameter> invalidParams = new ArrayList<>();

        if (valueUnset(matriculationStatus)) {
            invalidParams.add(getEmptyInvalidParameter(prefix));
        } else {
            ArrayList<String> existingFields = new ArrayList<>();

            List<String> validErIds = eUtil.getAllStates(stub, ExaminationRegulation.class).stream().map(ExaminationRegulation::getName).collect(Collectors.toList());
            for (int subMatIndex = 0; subMatIndex < matriculationStatus.size(); subMatIndex++) {

                SubjectMatriculation subMat = matriculationStatus.get(subMatIndex);

                if (valueUnset(subMat.getFieldOfStudy())) {
                    invalidParams.add(getEmptyInvalidParameter(prefix + "[" + subMatIndex + "].fieldOfStudy"));
                } else {
                    if (!validErIds.contains(subMat.getFieldOfStudy())) { // TODO check if examination regulation exists for the given semester
                        invalidParams.add(getInvalidFieldOfStudyParam(prefix + "[" + subMatIndex + "]."));
                    }
                    if (existingFields.contains(subMat.getFieldOfStudy())) {
                        invalidParams.add(getDuplicateFieldOfStudyParam(prefix, subMatIndex));
                    } else
                        existingFields.add(subMat.getFieldOfStudy());
                }

                List<String> semesters = subMat.getSemesters();
                if (valueUnset(semesters)) {
                    invalidParams.add(getEmptyInvalidParameter(prefix + "[" + subMatIndex + "].semesters"));
                }

                ArrayList<String> existingSemesters = new ArrayList<>();
                for (int semesterIndex = 0; semesterIndex < Objects.requireNonNull(semesters).size(); semesterIndex++) {

                    String semester = semesters.get(semesterIndex);

                    if (!semesterFormatValid(semester)) {
                        invalidParams.add(getInvalidSemesterParam(prefix + "[" + subMatIndex + "].semesters", semesterIndex));
                    } else {
                        if (semesterFormatValid(semester)) {
                            if (existingSemesters.contains(semester)) {
                                invalidParams.add(getDuplicateSemesterParam(prefix + "[" + subMatIndex + "].semesters", semesterIndex));
                            } else
                                existingSemesters.add(semester);
                        }
                    }
                }
            }
        }
        return invalidParams;
    }

    /**
     * Checks the given semester string for validity.
     *
     * @param semester semester string to check for validity
     * @return true if semester is a valid description of a semester, false otherwise
     */
    public boolean semesterFormatValid(String semester) {
        Pattern pattern = Pattern.compile("^(WS\\d{4}/\\d{2}|SS\\d{4})");
        Matcher matcher = pattern.matcher(semester);
        if (!matcher.matches())
            return false;
        if ("WS".equals(semester.substring(0, 2))) {
            int year1 = Integer.parseInt(semester.substring(4, 6));
            int year2 = Integer.parseInt(semester.substring(7, 9));
            return year2 == (year1 + 1) % 100;
        }
        return true;
    }

    public void checkParamsAddMatriculationData(Context ctx, String matriculationData) throws ParameterError {
        ChaincodeStub stub = ctx.getStub();

        MatriculationData newMatriculationData;
        try {
            newMatriculationData = GsonWrapper.fromJson(matriculationData, MatriculationData.class);
        } catch (Exception e) {
            throw new ParameterError(GsonWrapper.toJson(getUnprocessableEntityError(getUnparsableMatriculationDataParam())));
        }

        ArrayList<InvalidParameter> invalidParams = getErrorForMatriculationData(stub, newMatriculationData, "matriculationData");
        if (!invalidParams.isEmpty()) {
            throw new ParameterError(GsonWrapper.toJson(getUnprocessableEntityError(invalidParams)));
        }

        if (keyExists(stub, newMatriculationData.getEnrollmentId())) {
            throw new ParameterError(GsonWrapper.toJson(getConflictError()));
        }
    }

    public void checkParamsUpdateMatriculationData(Context ctx, String matriculationData) throws ParameterError {
        ChaincodeStub stub = ctx.getStub();

        MatriculationData newMatriculationData;
        try {
            newMatriculationData = GsonWrapper.fromJson(matriculationData, MatriculationData.class);
        } catch (Exception e) {
            throw new ParameterError(GsonWrapper.toJson(getUnprocessableEntityError(getUnparsableMatriculationDataParam())));
        }

        ArrayList<InvalidParameter> invalidParams = getErrorForMatriculationData(stub, newMatriculationData, "matriculationData");
        if (!invalidParams.isEmpty()) {
            throw new ParameterError(GsonWrapper.toJson(getUnprocessableEntityError(invalidParams)));
        }

        if (!keyExists(stub, newMatriculationData.getEnrollmentId())) {
            throw new ParameterError(GsonWrapper.toJson(getNotFoundError()));
        }
    }

    public void checkParamsGetMatriculationData(Context ctx, String enrollmentId) throws ParameterError {
        ChaincodeStub stub = ctx.getStub();
        try {
            getState(stub, enrollmentId, MatriculationData.class);
        } catch (LedgerAccessError e) {
            throw new ParameterError(e.getJsonError());
        }
    }

    public void checkParamsAddEntriesToMatriculationData(Context ctx, String enrollmentId, String matriculations) throws SerializableError {
        ChaincodeStub stub = ctx.getStub();

        ArrayList<InvalidParameter> invalidParams = new ArrayList<>();
        if (valueUnset(enrollmentId)) {
            invalidParams.add(getEmptyEnrollmentIdParam());
        }
        Type listType = new TypeToken<ArrayList<SubjectMatriculation>>() {}.getType();
        ArrayList<SubjectMatriculation> matriculationStatus;
        try {
            matriculationStatus = GsonWrapper.fromJson(matriculations, listType);
        } catch (Exception e) {
            invalidParams.add(getUnparsableMatriculationParam());
            throw new ParameterError(GsonWrapper.toJson(getUnprocessableEntityError(invalidParams)));
        }
        invalidParams.addAll(getErrorForSubjectMatriculationList(stub, matriculationStatus, "matriculations"));
        if (!invalidParams.isEmpty()) {
            throw new ParameterError(GsonWrapper.toJson(getUnprocessableEntityError(invalidParams)));
        }
        getState(stub, enrollmentId, MatriculationData.class);
    }
}