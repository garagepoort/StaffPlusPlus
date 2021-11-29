package net.shortninja.staffplus.core.domain.player.settings;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.database.PlayerSettingsEntity;
import net.shortninja.staffplus.core.application.session.database.PlayerSettingsSqlRepository;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@IocBean
public class PlayerSettingsRepository {
    private final Options options;
    private final PlayerSettingsSqlRepository playerSettingsSqlRepository;
    private final PlayerSettingsDataFile playerSettingsDataFile;
    private final FileConfiguration dataFileConfiguration;

    private final Map<UUID, PlayerSettings> settingsMap = new ConcurrentHashMap<>();

    public PlayerSettingsRepository(Options options,
                                    PlayerSettingsSqlRepository playerSettingsSqlRepository,
                                    PlayerSettingsDataFile playerSettingsDataFile) {
        this.options = options;
        this.playerSettingsSqlRepository = playerSettingsSqlRepository;
        this.dataFileConfiguration = playerSettingsDataFile.getConfiguration();
        this.playerSettingsDataFile = playerSettingsDataFile;
    }

    public PlayerSettings get(OfflinePlayer player) {
        if (settingsMap.containsKey(player.getUniqueId())) {
            return settingsMap.get(player.getUniqueId());
        }
        if (!dataFileConfiguration.contains(player.getUniqueId().toString())) {
            PlayerSettings settings = createSession(player);
            settingsMap.put(player.getUniqueId(), settings);
            return settings;
        }

        PlayerSettings playerSettings = retrieveSettings(player.getUniqueId());
        settingsMap.put(player.getUniqueId(), playerSettings);
        return playerSettings;
    }

    public synchronized PlayerSettings createSession(OfflinePlayer player) {
        if (dataFileConfiguration.contains(player.getUniqueId().toString())) {
            throw new PlayerSettingCreationException("A session for this player already exists. Cannot create new one.");
        }
        PlayerSettings playerSettings = new PlayerSettings(player.getUniqueId(),
            player.getName(),
            Material.WHITE_STAINED_GLASS_PANE,
            new HashSet<>(),
            VanishType.NONE,
                false,
            null,
            new HashSet<>());

        playerSettingsDataFile.save(playerSettings);
        if (options.serverSyncConfiguration.sessionSyncEnabled()) {
            updateSqlSettings(playerSettings);
        }
        settingsMap.put(player.getUniqueId(), playerSettings);
        return playerSettings;
    }

    private PlayerSettings retrieveSettings(UUID uuid) {
        String name = dataFileConfiguration.getString(uuid + ".name");
        String glassColor = dataFileConfiguration.getString(uuid + ".glass-color");

        Material glassMaterial = Material.WHITE_STAINED_GLASS_PANE;

        if (glassColor != null && !glassColor.equals("0")) {
            glassMaterial = Material.valueOf(glassColor);
        }

        List<String> playerNotes = loadPlayerNotes(uuid);
        Set<AlertType> alertOptions = loadAlertOptions(uuid);


        Optional<PlayerSettingsEntity> settingsEntity = playerSettingsSqlRepository.findSettings(uuid);
        VanishType vanishType = getVanishType(uuid, settingsEntity);
        Set<String> mutedChannels = settingsEntity.map(PlayerSettingsEntity::getMutedStaffChatChannels).orElse(new HashSet<>());

        boolean staffMode = dataFileConfiguration.getBoolean(uuid + ".staff-mode", false);
        String staffModeName = dataFileConfiguration.getString(uuid + ".staff-mode-name", null);
        if (options.serverSyncConfiguration.staffModeSyncEnabled && settingsEntity.isPresent()) {
            staffMode = settingsEntity.get().getStaffMode();
            staffModeName = settingsEntity.get().getStaffModeName();
        }

        return new PlayerSettings(uuid, name, glassMaterial, alertOptions, vanishType, staffMode, staffModeName, mutedChannels);
    }

    @NotNull
    private VanishType getVanishType(UUID uuid, Optional<PlayerSettingsEntity> settingsEntity) {
        VanishType vanishType = VanishType.valueOf(dataFileConfiguration.getString(uuid + ".vanish-type", "NONE"));
        if (options.serverSyncConfiguration.vanishSyncEnabled) {
            vanishType = settingsEntity.map(PlayerSettingsEntity::getVanishType).orElse(vanishType);
        }
        return vanishType;
    }

    private Set<AlertType> loadAlertOptions(UUID uuid) {
        Set<AlertType> alertOptions = new HashSet<>();

        for (String string : dataFileConfiguration.getStringList(uuid + ".alert-options")) {
            String[] parts = string.split(";");

            boolean enabled = Boolean.parseBoolean(parts[1]);
            if(enabled) {
                alertOptions.add(AlertType.valueOf(parts[0]));
            }
        }

        return alertOptions;
    }

    private List<String> loadPlayerNotes(UUID uuid) {
        List<String> playerNotes = new ArrayList<>();

        for (String string : dataFileConfiguration.getStringList(uuid + ".notes")) {
            if (string.contains("&7")) {
                continue;
            }

            playerNotes.add("&7" + string);
        }

        return playerNotes;
    }

    public synchronized void save(PlayerSettings playerSession) {
        playerSettingsDataFile.save(playerSession);
        if (options.serverSyncConfiguration.sessionSyncEnabled()) {
            updateSqlSettings(playerSession);
        }
        settingsMap.put(playerSession.getUuid(), playerSession);
    }

    private void updateSqlSettings(PlayerSettings playerSession) {
        Optional<PlayerSettingsEntity> session = playerSettingsSqlRepository.findSettings(playerSession.getUuid());
        String staffModeName = playerSession.getModeName().orElse(null);
        if (session.isPresent()) {
            if (options.serverSyncConfiguration.vanishSyncEnabled) {
                session.get().setVanishType(playerSession.getVanishType());
            }
            if (options.serverSyncConfiguration.staffModeSyncEnabled) {
                session.get().setStaffMode(playerSession.isInStaffMode());
                session.get().setStaffModeName(staffModeName);
            }
            session.get().setMutedStaffChatChannels(playerSession.getMutedStaffChatChannels());
            playerSettingsSqlRepository.update(session.get());
        } else {
            playerSettingsSqlRepository.saveSessions(new PlayerSettingsEntity(playerSession.getUuid(), playerSession.getVanishType(), playerSession.isInStaffMode(), playerSession.getMutedStaffChatChannels(), staffModeName));
        }
    }


}