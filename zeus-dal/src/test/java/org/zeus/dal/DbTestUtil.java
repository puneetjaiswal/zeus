package org.zeus.dal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javalite.activejdbc.Base;

import java.io.InputStream;
import java.util.Scanner;

public class DbTestUtil {
    public static void seedRequiredData(TestConfig testConfig) {
        String jdbcUrl = "jdbc:h2:" + testConfig.getH2DbFilePath();
        DataStoreConfiguration db = new DataStoreConfiguration(jdbcUrl, "sa", "sa", "org.h2.Driver");
        JdbcConnectionManager connectionManager = new JdbcConnectionManager(db);
        connectionManager.open();
        Base.exec(DbTestUtil.getResourceFileContent("zeus-persistence.sql"));
        connectionManager.close();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestConfig {
        private String configFilePath;
        private String h2DbFilePath;
    }

    public static String getResourceFileContent(String fileName) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream =
                DbTestUtil.class.getClassLoader().getResourceAsStream(fileName);
        Scanner scn = new Scanner(inputStream);
        while (scn.hasNextLine()) {
            sb.append(scn.nextLine()).append("\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
