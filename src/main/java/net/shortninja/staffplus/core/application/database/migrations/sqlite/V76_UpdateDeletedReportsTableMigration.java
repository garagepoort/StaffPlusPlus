package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V76_UpdateDeletedReportsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList("UPDATE sp_reports set deleted=0 WHERE deleted=false;","UPDATE sp_reports set deleted=1 WHERE deleted=true;");
    }

    @Override
    public int getVersion() {
        return 76;
    }
}
