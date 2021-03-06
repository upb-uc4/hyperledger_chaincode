package de.upb.cs.uc4.chaincode;


import de.upb.cs.uc4.chaincode.contract.matriculationdata.MatriculationDataContract;
import de.upb.cs.uc4.chaincode.model.JsonIOTest;
import de.upb.cs.uc4.chaincode.model.JsonIOTestSetup;
import de.upb.cs.uc4.chaincode.model.matriculation.MatriculationData;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import de.upb.cs.uc4.chaincode.contract.matriculationdata.MatriculationDataContractUtil;
import de.upb.cs.uc4.chaincode.util.TestUtil;
import org.hyperledger.fabric.contract.Context;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public final class MatriculationDataContractTest extends TestCreationBase {

    private final MatriculationDataContract contract = new MatriculationDataContract();
    private final MatriculationDataContractUtil cUtil = new MatriculationDataContractUtil();


    String getTestConfigDir() {
        return "src/test/resources/test_configs/matriculation_data_contract";
    }

    DynamicTest CreateTest(JsonIOTest test) {
        String testType = test.getType();
        String testName = test.getName();
        System.out.println(testName);
        JsonIOTestSetup setup = test.getSetup();
        List<String> input = TestUtil.toStringList(test.getInput());
        List<String> compare = TestUtil.toStringList(test.getCompare());
        List<String> ids = test.getIds();

        switch (testType) {
            case "getMatriculationData":
                return DynamicTest.dynamicTest(testName, getMatriculationDataTest(setup, input, compare, ids));
            case "addMatriculationData_SUCCESS":
                return DynamicTest.dynamicTest(testName, addMatriculationDataSuccessTest(setup, input, compare, ids));
            case "addMatriculationData_FAILURE":
                return DynamicTest.dynamicTest(testName, addMatriculationDataFailureTest(setup, input, compare, ids));
            case "updateMatriculationData_SUCCESS":
                return DynamicTest.dynamicTest(testName, updateMatriculationDataSuccessTest(setup, input, compare, ids));
            case "updateMatriculationData_FAILURE":
                return DynamicTest.dynamicTest(testName, updateMatriculationDataFailureTest(setup, input, compare, ids));
            case "addEntriesToMatriculationData_SUCCESS":
                return DynamicTest.dynamicTest(testName, addEntriesToMatriculationDataSuccessTest(setup, input, compare, ids));
            case "addEntriesToMatriculationData_FAILURE":
                return DynamicTest.dynamicTest(testName, addEntriesToMatriculationDataFailureTest(setup, input, compare, ids));
            default:
                throw new RuntimeException("Test " + testName + " of type " + testType + " could not be matched.");
        }
    }

    private Executable getMatriculationDataTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameGetMatriculationData, setup, input, ids);

            String getResult = contract.getMatriculationData(ctx, input.get(0));
            assertThat(getResult).isEqualTo(compare.get(0));
            MatriculationData compareMatriculationData = GsonWrapper.fromJson(compare.get(0), MatriculationData.class);
            MatriculationData ledgerMatriculationData = GsonWrapper.fromJson(getResult, MatriculationData.class);
            assertThat(ledgerMatriculationData).isEqualTo(compareMatriculationData);
            assertThat(ledgerMatriculationData.toString()).isEqualTo(compareMatriculationData.toString());
        };
    }

    private Executable addMatriculationDataSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameAddMatriculationData, setup, input, ids);

            String result = contract.addMatriculationData(ctx, input.get(0));
            assertThat(result).isEqualTo(compare.get(0));

            MatriculationData compareMatriculationData = GsonWrapper.fromJson(compare.get(0), MatriculationData.class);
            MatriculationData ledgerMatriculationData =
                    cUtil.getState(ctx.getStub(), compareMatriculationData.getEnrollmentId(), MatriculationData.class);
            assertThat(ledgerMatriculationData).isEqualTo(compareMatriculationData);
            assertThat(ledgerMatriculationData.toString()).isEqualTo(compareMatriculationData.toString());
        };
    }

    private Executable addMatriculationDataFailureTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameAddMatriculationData, setup, input, ids);

            String result = contract.addMatriculationData(ctx, input.get(0));
            assertThat(result).isEqualTo(compare.get(0));
        };
    }

    private Executable updateMatriculationDataSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameUpdateMatriculationData, setup, input, ids);

            String result = contract.updateMatriculationData(ctx, input.get(0));
            assertThat(result).isEqualTo(compare.get(0));

            MatriculationData compareMatriculationData = GsonWrapper.fromJson(compare.get(0), MatriculationData.class);
            MatriculationData ledgerMatriculationData =
                    cUtil.getState(ctx.getStub(), compareMatriculationData.getEnrollmentId(), MatriculationData.class);
            assertThat(ledgerMatriculationData).isEqualTo(compareMatriculationData);
            assertThat(ledgerMatriculationData.toString()).isEqualTo(compareMatriculationData.toString());
        };

    }

    private Executable updateMatriculationDataFailureTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameUpdateMatriculationData, setup, input, ids);

            String result = contract.updateMatriculationData(ctx, input.get(0));
            assertThat(result).isEqualTo(compare.get(0));
        };
    }

    private Executable addEntriesToMatriculationDataSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameAddEntriesToMatriculationData, setup, input, ids);

            String result = contract.addEntriesToMatriculationData(ctx, input.get(0), input.get(1));
            assertThat(result).isEqualTo(compare.get(0));

            MatriculationData compareMatriculationData = GsonWrapper.fromJson(compare.get(0), MatriculationData.class);
            MatriculationData ledgerMatriculationData =
                    cUtil.getState(ctx.getStub(), compareMatriculationData.getEnrollmentId(), MatriculationData.class);
            assertThat(ledgerMatriculationData).isEqualTo(compareMatriculationData);
            assertThat(ledgerMatriculationData.toString()).isEqualTo(compareMatriculationData.toString());
        };
    }

    private Executable addEntriesToMatriculationDataFailureTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(MatriculationDataContract.contractName, MatriculationDataContract.transactionNameAddEntriesToMatriculationData, setup, input, ids);

            String result = contract.addEntriesToMatriculationData(ctx, input.get(0), input.get(1));
            assertThat(result).isEqualTo(compare.get(0));
        };
    }
}