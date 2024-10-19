package com.example.habittracker;


import com.example.habittracker.app.ConsoleApp;
import com.example.habittracker.app.command.CommandContext;
import com.example.habittracker.config.DataSourceConfig;
import com.example.habittracker.config.LiquibaseMigrationConfig;

import javax.sql.DataSource;

public class HabitTrackerApp {
    public static void main(String[] args) {
        DataSource dataSource = DataSourceConfig.getDataSource();
        LiquibaseMigrationConfig migrationConfig = new LiquibaseMigrationConfig(dataSource);
        migrationConfig.updateDB();

        ConsoleApp app = new ConsoleApp(new CommandContext(dataSource));
        app.start();
    }
}