package com.example.habittracker.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

import static com.example.habittracker.util.DBUtil.createHikariConfig;
import static com.example.habittracker.util.DBUtil.loadProperties;

/**
 * The {@code DataSourceConfig} class provides utility methods for configuring and
 * obtaining a {@link javax.sql.DataSource} using the HikariCP connection pool.
 */
public class DataSourceConfig {

    private DataSourceConfig() {
    }

    /**
     * Returns a {@link javax.sql.DataSource} instance configured using properties loaded
     * by the {@code loadProperties()} method.
     * <p>
     * This method first loads the database configuration properties and then calls
     * {@link #getDataSource(Properties)} to create and return the data source.
     *
     * @return a configured {@link javax.sql.DataSource} instance
     */
    public static DataSource getDataSource() {
        Properties properties = loadProperties("dev");
        return getDataSource(properties);
    }

    /**
     * Returns a {@link javax.sql.DataSource} instance configured using the provided
     * {@link java.util.Properties}.
     * <p>
     * This method creates a {@link com.zaxxer.hikari.HikariConfig} from the given properties
     * and returns a new {@link com.zaxxer.hikari.HikariDataSource} based on the Hikari configuration.
     *
     * @param properties the database configuration properties used to set up the data source
     * @return a configured {@link javax.sql.DataSource} instance
     */
    public static DataSource getDataSource(Properties properties) {
        HikariConfig config = createHikariConfig(properties);
        return new HikariDataSource(config);
    }
}