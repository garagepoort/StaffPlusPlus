package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class V69_MigrateNotesToSqlMigration implements Migration {

    private static final String DATA_YML = "data.yml";

    @Override
    public List<String> getStatements() {
        Options options = StaffPlus.get().getIocContainer().get(Options.class);
        List<String> insertStatements = new ArrayList<>();

        File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = configuration.getKeys(false);
        keys.stream()
            .filter(key -> configuration.contains(key + ".notes"))
            .forEach(key -> {
                String targetName = getPlayerName(UUID.fromString(key)).orElse("Unknown");
                List<String> notes = configuration.getStringList(key + ".notes");
                notes.stream()
                    .map(note -> buildInsertNoteStatement(key, targetName, note, options.serverName))
                    .forEach(insertStatements::add);
                configuration.set(key + ".notes", null);
            });
        return insertStatements;
    }

    private String buildInsertNoteStatement(String key, String targetName, String note, String serverName) {
        return String.format(
            "INSERT INTO sp_player_notes " +
                "(note, target_name, target_uuid, noted_by_name, noted_by_uuid, creation_timestamp, server_name) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s', %s, '%s');",
            note.replace("'", "''"), targetName, key, "Console", Constants.CONSOLE_UUID.toString(), System.currentTimeMillis(), serverName.replace("'", "''"));
    }

    public Optional<String> getPlayerName(UUID uuid) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
            .filter(p -> p.getUniqueId().equals(uuid))
            .findFirst()
            .map(OfflinePlayer::getName);
    }

    @Override
    public int getVersion() {
        return 69;
    }
}
