package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.Migrations;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

@IocBean(priority = true)
public class DatabaseInitializer {

    public DatabaseInitializer(SqlConnectionProvider sqlConnectionProvider) {
        new Migrations(sqlConnectionProvider).run("migrations");
    }

}
