package de.upb.cs.uc4.chaincode;


import com.google.gson.reflect.TypeToken;
import de.upb.cs.uc4.chaincode.model.JsonIOTest;
import de.upb.cs.uc4.chaincode.helper.GsonWrapper;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TestCreationBase {

    abstract DynamicTest CreateTest(JsonIOTest test);

    abstract String getTestConfigDir();

    @TestFactory
    List<DynamicTest> createTests() {
        String testConfigDir = getTestConfigDir();
        File dir = new File(testConfigDir);
        File[] testConfigs = dir.listFiles();

        List<JsonIOTest> testConfig;
        Type type = new TypeToken<List<JsonIOTest>>() {}.getType();
        ArrayList<DynamicTest> tests = new ArrayList<>();

        if (testConfigs == null) {
            throw new RuntimeException("No test configurations found.");
        }

        for (File file: testConfigs) {
            try {
                testConfig = GsonWrapper.fromJson(new FileReader(file.getPath()), type);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            for (JsonIOTest test : testConfig) {
                test.setIds(wrapEnrollmentIds(test.getIds()));
                tests.add(CreateTest(test));
            }
        }
        return tests;
    }

    private List<String> wrapEnrollmentIds(List<String> ids) {
        return ids.stream().map(id -> "x509::CN=" + id + ", OU=admin::CN=rca-org1, OU=UC4, O=UC4, L=Paderborn, ST=NRW, C=DE").collect(Collectors.toList());
    }
}
