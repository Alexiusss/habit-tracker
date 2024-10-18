package com.example.habittracker.util;

import com.example.habittracker.config.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The {@code DBUtil} class provides utility methods to manage database operations such as loading
 * configuration properties, creating HikariCP configurations, and managing Liquibase database
 * migrations.
 */
@UtilityClass
public class DBUtil {

    public static final String PROPS_FILE = "application-default.properties";
    public static final String ENV_FILE = ".env";
    public static final String LIQUIBASE_SERVICE_SCHEMA_NAME = "service";
    public static final String CREATE_SCHEMA_SQL = "CREATE SCHEMA IF NOT EXISTS ";
    public static final String CHANGE_LOG_PATH = "db/changelog/changelog.xml";


    /**
     * This method loads database configuration properties from two sources:
     * <ul>
     *     <li>An {@code application-dev.properties} file located in the classpath.</li>
     *     <li>A {@code .env} file located in the root directory.</li>
     * </ul>
     * <p>
     *  The values in these files are used to initialize the database URL, username, and password
     **/
    public static Properties loadProperties(String currentProfile) {
        Properties props = new Properties();
        try (InputStream is = DataSourceConfig.class.getClassLoader().getResourceAsStream(PROPS_FILE.replace("default", currentProfile));
             FileReader fr = new FileReader(ENV_FILE)) {
            props.load(is);
            props.load(fr);
        } catch (IOException e) {
            System.out.println("Exception while loading properties from file: \n" + e.getMessage());
        }
        return props;
    }

    /**
     * Creates and configures a {@link com.zaxxer.hikari.HikariConfig} using the given properties.
     * <p>
     * The method replaces placeholders for the database URL, username, and password
     * with values provided in the properties.
     *
     * @param properties the {@link java.util.Properties} containing the database connection details
     * @return a configured {@link com.zaxxer.hikari.HikariConfig} instance
     */
    public static HikariConfig createHikariConfig(Properties properties) {
        final String jdbc_url = properties.getProperty("datasource.url").replace("${POSTGRES_DB}", properties.getProperty("POSTGRES_DB"));
        final String user = properties.getProperty("datasource.username").replace("${POSTGRES_USER}", properties.getProperty("POSTGRES_USER"));
        final String password = properties.getProperty("datasource.password").replace("${POSTGRES_PASSWORD}", properties.getProperty("POSTGRES_PASSWORD"));

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbc_url);
        config.setUsername(user);
        config.setPassword(password);
        return config;
    }

    /**
     * Creates the Liquibase service schema if it does not already exist.
     *
     * @param connection the {@link java.sql.Connection} to the database
     */
    public static void createLiquibaseServiceSchema(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_SCHEMA_SQL + LIQUIBASE_SERVICE_SCHEMA_NAME);
        } catch (SQLException e) {
            System.out.println("Exception while creating a schema " + LIQUIBASE_SERVICE_SCHEMA_NAME + ":\n " + e.getMessage());
        }
    }

    /**
     * Creates and returns a {@link liquibase.Liquibase} instance using the provided connection.
     * <p>
     * This method sets up the Liquibase environment using the changelog file.
     *
     * @param connection the {@link java.sql.Connection} to be used by Liquibase
     * @return a configured {@link liquibase.Liquibase} instance
     */
    public static Liquibase createLiquibase(Connection connection) {
        Database database = createDatabase(connection);
        return new Liquibase(CHANGE_LOG_PATH, new ClassLoaderResourceAccessor(), database);
    }

    /**
     * Creates and returns a {@link liquibase.database.Database} instance using the provided connection.
     * <p>
     * This method initializes a {@link liquibase.database.jvm.JdbcConnection} and sets
     * the Liquibase schema name for the connection.
     *
     * @param connection the {@link java.sql.Connection} to be used by the database
     * @return a {@link liquibase.database.Database} object or {@code null} if an exception occurs
     */
    private static Database createDatabase(Connection connection) {
        Database database = null;
        try {
            database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(LIQUIBASE_SERVICE_SCHEMA_NAME);
        } catch (DatabaseException e) {
            System.out.println("Exception while creating a database: \n " + e.getMessage());
        }

        return database;
    }
}
