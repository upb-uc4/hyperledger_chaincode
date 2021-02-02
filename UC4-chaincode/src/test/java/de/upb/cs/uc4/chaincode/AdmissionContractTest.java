package de.upb.cs.uc4.chaincode;

import com.google.gson.reflect.TypeToken;
import de.upb.cs.uc4.chaincode.contract.admission.AdmissionContract;
import de.upb.cs.uc4.chaincode.model.*;
import de.upb.cs.uc4.chaincode.contract.admission.AdmissionContractUtil;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import de.upb.cs.uc4.chaincode.model.admission.CourseAdmission;
import de.upb.cs.uc4.chaincode.util.TestUtil;
import org.hyperledger.fabric.contract.Context;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class AdmissionContractTest extends TestCreationBase {

    private final AdmissionContract contract = new AdmissionContract();
    private final AdmissionContractUtil cUtil = new AdmissionContractUtil();

    String getTestConfigDir() {
        return "src/test/resources/test_configs/admission_contract";
    }

    DynamicTest CreateTest(JsonIOTest test) {
        String testType = test.getType();
        String testName = test.getName();
        JsonIOTestSetup setup = test.getSetup();
        List<String> input = TestUtil.toStringList(test.getInput());
        List<String> compare = TestUtil.toStringList(test.getCompare());
        List<String> ids = test.getIds();

        switch (testType) {
            case "addAdmission_SUCCESS":
                return DynamicTest.dynamicTest(testName, addAdmissionSuccessTest(setup, input, compare, ids));
            case "addAdmission_FAILURE":
                return DynamicTest.dynamicTest(testName, addAdmissionFailureTest(setup, input, compare, ids));
            case "dropAdmission_SUCCESS":
                return DynamicTest.dynamicTest(testName, dropAdmissionSuccessTest(setup, input, compare, ids));
            case "dropAdmission_FAILURE":
                return DynamicTest.dynamicTest(testName, dropAdmissionFailureTest(setup, input, compare, ids));
            case "getAdmissions_SUCCESS":
                return DynamicTest.dynamicTest(testName, getAdmissionsSuccessTest(setup, input, compare, ids));
            default:
                throw new RuntimeException("Test " + testName + " of type " + testType + " could not be matched.");
        }
    }

    private Executable addAdmissionSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(AdmissionContract.contractName, AdmissionContract.transactionNameAddAdmission, setup, input, ids);

            String addResult = contract.addAdmission(ctx, input.get(0));

            assertThat(addResult).isEqualTo(compare.get(0));
            CourseAdmission compareAdmission = GsonWrapper.fromJson(compare.get(0), CourseAdmission.class);
            CourseAdmission ledgerAdmission = cUtil.getState(ctx.getStub(), compareAdmission.getAdmissionId(), CourseAdmission.class);
            assertThat(ledgerAdmission).isEqualTo(compareAdmission);
            assertThat(ledgerAdmission.toString()).isEqualTo(compareAdmission.toString());
        };
    }

    private Executable addAdmissionFailureTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(AdmissionContract.contractName, AdmissionContract.transactionNameAddAdmission, setup, input, ids);
            String result = contract.addAdmission(ctx, input.get(0));
            assertThat(result).isEqualTo(compare.get(0));
        };
    }

    private Executable dropAdmissionSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(AdmissionContract.contractName, AdmissionContract.transactionNameDropAdmission, setup, input, ids);

            String dropResult = contract.dropAdmission(ctx, input.get(0));

            assertThat(dropResult).isEqualTo(compare.get(0));
            List<CourseAdmission> ledgerState = cUtil.getAllStates(ctx.getStub(), CourseAdmission.class);
            assertThat(ledgerState).allMatch(item -> !(item.getAdmissionId().equals(input.get(0))));
        };
    }

    private Executable dropAdmissionFailureTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(AdmissionContract.contractName, AdmissionContract.transactionNameDropAdmission, setup, input, ids);

            String result = contract.dropAdmission(ctx, input.get(0));
            assertThat(result).isEqualTo(compare.get(0));
        };
    }

    private Executable getAdmissionsSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            Context ctx = TestUtil.buildContext(AdmissionContract.contractName, AdmissionContract.transactionNameGetAdmissions, setup, input, ids);

            String getResult = contract.getCourseAdmissions(ctx, input.get(0), input.get(1), input.get(2));
            assertThat(getResult).isEqualTo(compare.get(0));
        };
    }
}
