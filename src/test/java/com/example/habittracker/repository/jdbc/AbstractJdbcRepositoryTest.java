package com.example.habittracker.repository.jdbc;

import com.example.habittracker.config.LiquibaseMigrationConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.Properties;

import static com.example.habittracker.config.DataSourceConfig.getDataSource;
import static com.example.habittracker.util.DBUtil.loadProperties;

@Testcontainers
public class AbstractJdbcRepositoryTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:12-alpine"
    );

    static DataSource dataSource;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        Properties props = getPropertiesWithReplacedPostgresPort();
        dataSource = getDataSource(props);
        initDB(dataSource);
    }


    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    private static Properties getPropertiesWithReplacedPostgresPort() {
        String currentContainerPort = postgres.getFirstMappedPort().toString();
        Properties properties = loadProperties("test");
        String replaced = properties.getProperty("datasource.url").replace("5432", currentContainerPort);
        properties.replace("datasource.url", replaced);
        return properties;
    }

    private static void initDB(DataSource dataSource) {
        LiquibaseMigrationConfig migrationConfig = new LiquibaseMigrationConfig(dataSource);
        migrationConfig.updateDB();
    }
}