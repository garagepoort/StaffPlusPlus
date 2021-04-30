package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V54_AddNamesToWarningsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addPlayerNameColumn = "ALTER TABLE sp_warnings ADD COLUMN player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addIssuerNameColumn = "ALTER TABLE sp_warnings ADD COLUMN warner_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);
        statements.add(addIssuerNameColumn);

        List<String> updates = Arrays.stream(Bukkit.getOfflinePlayers()).flatMap(p -> Stream.of(
            String.format("UPDATE sp_warnings set player_name='%s' WHERE Player_UUID='%s';", p.getName(), p.getUniqueId()),
            String.format("UPDATE sp_warnings set warner_name='%s' WHERE Warner_UUID='%s';", p.getName(), p.getUniqueId())))
            .collect(Collectors.toList());
        statements.addAll(updates);
        return statements;
    }

    @Override
    public int getVersion() {
        return 54;
    }
}
