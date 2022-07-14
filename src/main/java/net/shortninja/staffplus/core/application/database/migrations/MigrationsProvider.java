package net.shortninja.staffplus.core.application.database.migrations;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcsqlmigrations.Migration;

import java.util.List;

@IocBean
public class MigrationsProvider implements be.garagepoort.mcsqlmigrations.MigrationsProvider {
    private final List<Migration> migrations;

    public MigrationsProvider(@IocMulti(Migration.class) List<Migration> migrations) {
        this.migrations = migrations;
    }

    @Override
    public List<? extends Migration> getMigrations() {
        return migrations;
    }
}
