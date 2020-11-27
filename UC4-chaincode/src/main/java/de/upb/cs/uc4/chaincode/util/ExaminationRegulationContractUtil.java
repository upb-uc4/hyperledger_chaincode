package de.upb.cs.uc4.chaincode.util;

import de.upb.cs.uc4.chaincode.model.*;
import de.upb.cs.uc4.chaincode.model.errors.GenericError;
import de.upb.cs.uc4.chaincode.model.errors.InvalidParameter;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.*;

public class ExaminationRegulationContractUtil extends ContractUtil {
    private final String thing = "examination regulation";
    private final String identifier = "name";
    private final String prefix = "examinationRegulation";

    public ExaminationRegulationContractUtil() {
        keyPrefix = "examination-regulation";
    }

    @Override
    public GenericError getConflictError() {
        return super.getConflictError(thing, identifier);
    }

    @Override
    public GenericError getNotFoundError() {
        return super.getNotFoundError(thing, identifier);
    }

    public InvalidParameter getUnparsableExaminationRegulationParam() {
        return new InvalidParameter()
                .name(prefix)
                .reason("The given parameter cannot be parsed from json");
    }

    public InvalidParameter getUnparsableNameListParam() {
        return new InvalidParameter()
                .name("names")
                .reason("The given parameter cannot be parsed from json");
    }

    public InvalidParameter getDuplicateModuleParam(String parameterName) {
        return new InvalidParameter()
                .name(parameterName)
                .reason("Each module must only appear once in examinationRegulation.modules");
    }

    public InvalidParameter getInconsistentModuleParam(String parameterName) {
        return new InvalidParameter()
                .name(parameterName)
                .reason("Each module must be consistent with the modules on chain, i.e. if the module.id is equal, then module.name must be too");
    }

    public HashSet<ExaminationRegulationModule> getValidModules(ChaincodeStub stub) {
        HashSet<ExaminationRegulationModule> validModules = new HashSet<>();
        List<ExaminationRegulation> regulations = getAllStates(stub, ExaminationRegulation.class);
        for (ExaminationRegulation regulation: regulations) {
            validModules.addAll(regulation.getModules());
        }
        return validModules;
    }

    public ArrayList<InvalidParameter> getErrorForExaminationRegulation(ExaminationRegulation examinationRegulation, Set<ExaminationRegulationModule> validModules) {
        ArrayList<InvalidParameter> invalidParams = new ArrayList<>();

        if(valueUnset(examinationRegulation.getName())) {
            invalidParams.add(getEmptyParameterError(this.prefix+".name"));
        }

        invalidParams.addAll(getErrorForModuleList(
                examinationRegulation.getModules(),
                this.prefix+".modules",
                validModules));
        return invalidParams;
    }

    public ArrayList<InvalidParameter> getErrorForModuleList(List<ExaminationRegulationModule> modules, String errorName, Set<ExaminationRegulationModule> validModules) {
        ArrayList<InvalidParameter> invalidParams = new ArrayList<>();

        if (valueUnset(modules)) {
            invalidParams.add(getEmptyParameterError(errorName));
        } else {
            ArrayList<String> existingModules = new ArrayList<>();

            for (int moduleIndex=0; moduleIndex<modules.size(); moduleIndex++) {

                ExaminationRegulationModule module = modules.get(moduleIndex);

                if (valueUnset(module.getId())) {
                    invalidParams.add(getEmptyParameterError(prefix + ".modules[" + moduleIndex + "].id"));
                } else {
                    if (existingModules.contains(module.getId())) {
                        invalidParams.add(getDuplicateModuleParam(prefix + ".modules[" + moduleIndex + "]"));
                    } else
                        existingModules.add(module.getId());
                }
                if (valueUnset(module.getName())) {
                    invalidParams.add(getEmptyParameterError(prefix + ".modules[" + moduleIndex + "].name"));
                }
                for (ExaminationRegulationModule validModule: validModules) {
                    if (module.getId().equals(validModule.getId()) && !module.equals(validModule)) {
                        invalidParams.add(getInconsistentModuleParam(prefix + ".modules[" + moduleIndex + "]"));
                        break;
                    }
                }
            }
        }
        return invalidParams;
    }
}
