package com.rmarcello.starevent;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.MySQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class TestLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private static final MySQLContainer  DATABASE = new MySQLContainer<>("mysql:8.0.22")
            .withDatabaseName("eventdb")
            .withUsername("someuser")
            .withPassword("somepassword")
            .withExposedPorts(9999);

    @Override
    public Map<String, String> start() {
        DATABASE.start();
        Map<String, String> map = new HashMap<>();
        map.put("quarkus.datasource.jdbc.url", DATABASE.getJdbcUrl());
        map.put("quarkus.datasource.username", DATABASE.getUsername());
        map.put("quarkus.datasource.password", DATABASE.getPassword());
        map.put("quarkus.hibernate-orm.database.generation", "drop-and-create");
        return map;
    }

    @Override
    public void stop() {
        DATABASE.stop();
    }
    
}
