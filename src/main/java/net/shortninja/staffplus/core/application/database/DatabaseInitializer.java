package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.Migrations;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;

@IocBean(priority = true)
public class DatabaseInitializer {

    // PlayerIpRepository is injected here to ensure the migrations can use them, do not remove it
    public DatabaseInitializer(SqlConnectionProvider sqlConnectionProvider, PlayerIpRepository playerIpRepository) {
        new Migrations(sqlConnectionProvider).run("migrations");
    }

}
