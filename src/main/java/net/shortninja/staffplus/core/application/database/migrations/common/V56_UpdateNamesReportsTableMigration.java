package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class V56_UpdateNamesReportsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.stream(Bukkit.getOfflinePlayers()).flatMap(p -> Stream.of(
            String.format("UPDATE sp_reports set player_name='%s' WHERE Player_UUID='%s';", p.getName(), p.getUniqueId()),
            String.format("UPDATE sp_reports set reporter_name='%s' WHERE Reporter_UUID='%s';", p.getName(), p.getUniqueId())))
            .collect(Collectors.toList());
    }

    @Override
    public int getVersion() {
        return 56;
    }
}
