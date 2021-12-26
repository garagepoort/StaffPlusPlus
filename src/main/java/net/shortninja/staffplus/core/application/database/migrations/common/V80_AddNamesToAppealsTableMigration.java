package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V80_AddNamesToAppealsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addAppealerName = "ALTER TABLE sp_appeals ADD COLUMN appealer_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addResolverName = "ALTER TABLE sp_appeals ADD COLUMN resolver_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addAppealerName);
        statements.add(addResolverName);

        List<String> updates = Arrays.stream(Bukkit.getOfflinePlayers()).flatMap(p -> Stream.of(
            String.format("UPDATE sp_appeals set appealer_name='%s' WHERE appealer_uuid='%s';", p.getName(), p.getUniqueId()),
            String.format("UPDATE sp_appeals set resolver_name='%s' WHERE resolver_uuid='%s';", p.getName(), p.getUniqueId())))
            .collect(Collectors.toList());
        statements.addAll(updates);
        return statements;
    }

    @Override
    public int getVersion() {
        return 80;
    }
}
