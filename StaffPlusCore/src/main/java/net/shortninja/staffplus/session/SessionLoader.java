package net.shortninja.staffplus.session;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.database.SessionEntity;
import net.shortninja.staffplus.session.database.SessionsRepository;
import net.shortninja.staffplus.staff.mute.MuteService;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.VanishType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

import static org.bukkit.Bukkit.getScheduler;

public class SessionLoader {
    private final FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private final PlayerManager playerManager;
    private final MuteService muteService;
    private final Options options;
    private final SessionsRepository sessionsRepository;

    public SessionLoader(PlayerManager playerManager, MuteService muteService, Options options, SessionsRepository sessionsRepository) {
        this.playerManager = playerManager;
        this.muteService = muteService;
        this.options = options;
        this.sessionsRepository = sessionsRepository;
    }

    PlayerSession loadSession(Player player) {
        return loadSession(player.getUniqueId());
    }

    PlayerSession loadSession(UUID playerUuid) {
        return dataFile.contains(playerUuid.toString()) ? buildKnownSession(playerUuid) : buildNewSession(playerUuid);
    }

    private PlayerSession buildNewSession(UUID uuid) {
        Optional<SppPlayer> providedPlayer = playerManager.getOnlinePlayer(uuid);
        if (!providedPlayer.isPresent()) {
            throw new RuntimeException("Trying to instantiate session for offline user");
        }
        return new PlayerSession(uuid, providedPlayer.get().getUsername(), isMuted(uuid));
    }

    private PlayerSession buildKnownSession(UUID uuid) {
        String name = dataFile.getString(uuid + ".name");
        String glassColor = dataFile.getString(uuid + ".glass-color");
        VanishType vanishType = VanishType.valueOf(dataFile.getString(uuid + ".vanish-type", "NONE"));
        boolean staffMode = dataFile.getBoolean(uuid + ".staff-mode", false);
        Material glassMaterial = Material.WHITE_STAINED_GLASS_PANE;
        if (glassColor != null && !glassColor.equals("0")) {
            glassMaterial = Material.valueOf(glassColor);
        }

        List<String> playerNotes = loadPlayerNotes(uuid);
        Map<AlertType, Boolean> alertOptions = loadAlertOptions(uuid);

        PlayerSession playerSession = new PlayerSession(uuid, name, glassMaterial, playerNotes, alertOptions, isMuted(uuid), vanishType, staffMode);
        enhanceWithSyncedData(uuid, vanishType, staffMode, playerSession);
        return playerSession;
    }

    private void enhanceWithSyncedData(UUID uuid, VanishType vanishType, boolean staffMode, PlayerSession playerSession) {
        Optional<SessionEntity> session = sessionsRepository.findSession(uuid);
        if (options.serverSyncConfiguration.isStaffModeSyncEnabled()) {
            playerSession.setInStaffMode(session.map(SessionEntity::getStaffMode).orElse(staffMode));
        }
        if (options.serverSyncConfiguration.isVanishSyncEnabled()) {
            playerSession.setVanishType(session.map(SessionEntity::getVanishType).orElse(vanishType));
        }
    }

    private boolean isMuted(UUID uuid) {
        return muteService.getMuteByMutedUuid(uuid).isPresent();
    }

    private Map<AlertType, Boolean> loadAlertOptions(UUID uuid) {
        Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();

        for (String string : dataFile.getStringList(uuid + ".alert-options")) {
            String[] parts = string.split(";");

            alertOptions.put(AlertType.valueOf(parts[0]), Boolean.valueOf(parts[1]));
        }

        return alertOptions;
    }

    private List<String> loadPlayerNotes(UUID uuid) {
        List<String> playerNotes = new ArrayList<String>();

        for (String string : dataFile.getStringList(uuid + ".notes")) {
            if (string.contains("&7")) {
                continue;
            }

            playerNotes.add("&7" + string);
        }

        return playerNotes;
    }

    public void saveSession(PlayerSession playerSession) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            save(playerSession);
        });
    }

    public void saveSessionSynchronous(PlayerSession playerSession) {
        save(playerSession);
    }

    private void save(PlayerSession playerSession) {
        new Save(playerSession);
        StaffPlus.get().dataFile.save();

        if(options.serverSyncConfiguration.sessionSyncEnabled()) {
            Optional<SessionEntity> session = sessionsRepository.findSession(playerSession.getUuid());
            if (session.isPresent()) {
                if(options.serverSyncConfiguration.isVanishSyncEnabled()) {
                    session.get().setVanishType(playerSession.getVanishType());
                }
                if(options.serverSyncConfiguration.isStaffModeSyncEnabled()) {
                    session.get().setStaffMode(playerSession.isInStaffMode());
                }
                sessionsRepository.update(session.get());
            } else {
                sessionsRepository.addSession(new SessionEntity(playerSession.getUuid(), playerSession.getVanishType(), playerSession.isInStaffMode()));
            }
        }
    }
}