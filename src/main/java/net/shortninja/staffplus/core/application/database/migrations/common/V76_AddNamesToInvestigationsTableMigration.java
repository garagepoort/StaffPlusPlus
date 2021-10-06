package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V76_AddNamesToInvestigationsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addInvestigatorName = "ALTER TABLE sp_investigations ADD COLUMN investigator_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addInvestigatedName = "ALTER TABLE sp_investigations ADD COLUMN investigated_name VARCHAR(32) NULL;";
        List<String> statements = new ArrayList<>();
        statements.add(addInvestigatedName);
        statements.add(addInvestigatorName);

        List<String> updates = Arrays.stream(Bukkit.getOfflinePlayers()).flatMap(p -> Stream.of(
            String.format("UPDATE sp_investigations set investigator_name='%s' WHERE investigator_uuid='%s';", p.getName(), p.getUniqueId()),
            String.format("UPDATE sp_investigations set investigated_name='%s' WHERE investigated_uuid='%s';", p.getName(), p.getUniqueId())))
            .collect(Collectors.toList());
        statements.addAll(updates);
        return statements;
    }

    @Override
    public int getVersion() {
        return 76;
    }
}
