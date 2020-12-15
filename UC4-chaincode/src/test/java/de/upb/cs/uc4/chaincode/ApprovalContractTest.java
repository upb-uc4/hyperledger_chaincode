package de.upb.cs.uc4.chaincode;


import de.upb.cs.uc4.chaincode.mock.MockChaincodeStub;
import de.upb.cs.uc4.chaincode.model.*;
import de.upb.cs.uc4.chaincode.util.ApprovalContractUtil;
import de.upb.cs.uc4.chaincode.util.TestUtil;
import de.upb.cs.uc4.chaincode.util.helper.GsonWrapper;
import org.hyperledger.fabric.contract.Context;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public final class ApprovalContractTest extends TestCreationBase {

    private final ApprovalContract contract = new ApprovalContract();
    private final ApprovalContractUtil cUtil = new ApprovalContractUtil();

    String getTestConfigDir() {
        return "src/test/resources/test_configs/approval_contract";
    }

    DynamicTest CreateTest(JsonIOTest test) {
        String testType = test.getType();
        String testName = test.getName();
        JsonIOTestSetup setup = test.getSetup();
        List<String> input = TestUtil.toStringList(test.getInput());
        List<String> compare = TestUtil.toStringList(test.getCompare());
        List<String> ids = test.getIds();

        switch (testType) {
            case "getApprovals":
                return DynamicTest.dynamicTest(testName, getApprovalsTest(setup, input, compare));
            case "approveTransaction_SUCCESS":
                return DynamicTest.dynamicTest(test.getName(), approveTransactionSuccessTest(setup, input, compare, ids));
            case "approveTransaction_FAILURE":
                return DynamicTest.dynamicTest(test.getName(), approveTransactionFailureTest(setup, input, compare, ids));
            default:
                throw new RuntimeException("Test " + testName + " of type " + testType + " could not be matched.");
        }
    }

    private Executable getApprovalsTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare
    ) {
        return () -> {
            MockChaincodeStub stub = TestUtil.mockStub(setup);
            Context ctx = TestUtil.mockContext(stub);

            String approvals = contract.getApprovals(ctx, contract(input), transaction(input), params(input));
            assertThat(approvals).isEqualTo(compare.get(0));
        };
    }

    private Executable approveTransactionSuccessTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            MockChaincodeStub stub = TestUtil.mockStub(setup);
            for (int i=0; i< ids.size(); i++) {
                Context ctx = TestUtil.mockContext(stub, ids.get(i));
                SubmissionResult compareResult = GsonWrapper.fromJson(compare.get(i), SubmissionResult.class);
                SubmissionResult transactionResult = GsonWrapper.fromJson(contract.approveTransaction(ctx, contract(input), transaction(input), params(input)), SubmissionResult.class);
                assertThat(GsonWrapper.toJson(transactionResult)).isEqualTo(GsonWrapper.toJson(compareResult)); // TODO remove serialization
            }
            Context ctx = TestUtil.mockContext(stub);
            String key = cUtil.getDraftKey(contract(input), transaction(input), params(input));
            ApprovalList compareApproval = GsonWrapper.fromJson(compare.get(compare.size()-1), SubmissionResult.class).getExistingApprovals();
            ApprovalList ledgerApproval = cUtil.getState(ctx.getStub(), key, ApprovalList.class);
            assertThat(GsonWrapper.toJson(compareApproval)).isEqualTo(GsonWrapper.toJson(ledgerApproval)); // TODO remove serialization
        };
    }

    private Executable approveTransactionFailureTest(
            JsonIOTestSetup setup,
            List<String> input,
            List<String> compare,
            List<String> ids
    ) {
        return () -> {
            MockChaincodeStub stub = TestUtil.mockStub(setup);
            for (String s : compare) {
                Context ctx = cUtil.valueUnset(ids) ? TestUtil.mockContext(stub) : TestUtil.mockContext(stub, ids.get(0));
                String result = contract.approveTransaction(ctx, contract(input), transaction(input), params(input));
                assertThat(result).isEqualTo(s);
            }
        };
    }

    private String contract(List<String> input) {
        return input.get(0);
    }

    private String transaction(List<String> input) {
        return input.get(1);
    }

    private String params(List<String> input) {
        return GsonWrapper.toJson(input.subList(2, input.size()));
    }
}