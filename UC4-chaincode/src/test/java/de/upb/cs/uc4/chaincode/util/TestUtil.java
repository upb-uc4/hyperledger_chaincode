package de.upb.cs.uc4.chaincode.util;

import de.upb.cs.uc4.chaincode.mock.MockChaincodeStub;
import de.upb.cs.uc4.chaincode.model.Dummy;
import de.upb.cs.uc4.chaincode.model.JsonIOTestSetup;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtil {
    public static List<String> toStringList(List<Dummy> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(Dummy::getContent).collect(Collectors.toList());
    }

    public static Context mockContext(MockChaincodeStub stub) {
        return mockContext(stub, "testId");
    }

    public static Context mockContext(MockChaincodeStub stub, String clientId) {
        Context ctx = mock(Context.class);
        when(ctx.getStub()).thenReturn(stub);
        stub.setCurrentId(clientId);
        ClientIdentity testId = mock(ClientIdentity.class);
        when(testId.getId()).thenReturn(clientId);
        when(ctx.getClientIdentity()).thenReturn(testId);
        return ctx;
    }

    public static MockChaincodeStub mockStub(JsonIOTestSetup setup) {
        MockChaincodeStub stub = new MockChaincodeStub();
        setup.prepareStub(stub);
        return stub;
    }

    public void setTransientMap(MockChaincodeStub stub, List<Dummy> input) {
        stub.setTransient(
                input.stream().collect(
                        Collectors.toMap(
                                entry -> String.valueOf(input.indexOf(entry)),
                                entry -> entry.getContent().getBytes())));
    }

    public static String jsonListParams(List<String> params) {
        // TODO utilize gson for this
        return "[" + params.stream().reduce((s1, s2) -> s1 + "," + s2).orElse("") + "]";
    }
}
