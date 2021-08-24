package net.shortninja.staffplus.core.domain.staff.mute;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean
public class MuteSessionTask extends BukkitRunnable {
    public static final int DELAY = 10 * 20;

    private final Messages messages;
    private final OnlineSessionsManager onlineSessionsManager;
    private final MuteService muteService;
    private final MuteConfiguration muteConfiguration;
    private final PlayerManager playerManager;

    public MuteSessionTask(Messages messages, OnlineSessionsManager onlineSessionsManager, MuteService muteService, MuteConfiguration muteConfiguration, PlayerManager playerManager) {
        this.messages = messages;
        this.onlineSessionsManager = onlineSessionsManager;
        this.muteService = muteService;
        this.muteConfiguration = muteConfiguration;
        this.playerManager = playerManager;
        runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
    }

    @Override
    public void run() {
        if (!muteConfiguration.muteEnabled) {
            return;
        }

        ArrayList<Player> onlinePlayers = new ArrayList<>(playerManager.getOnlinePlayers());
        List<Mute> activeMutes = muteService.getAllActiveMutes(onlinePlayers);
        List<OnlinePlayerSession> mutedSessions = onlineSessionsManager.getAll()
            .stream()
            .filter(OnlinePlayerSession::isMuted)
            .collect(Collectors.toList());

        mutedSessions.forEach(mutedSession -> {
            Optional<Player> onlinePlayer = getPlayer(onlinePlayers, mutedSession);
            if (onlinePlayer.isPresent()) {
                Optional<Mute> activeMute = activeMutes.stream().filter(mute -> mute.getTargetUuid().equals(onlinePlayer.get().getUniqueId())).findFirst();
                mutedSession.setMuted(activeMute.isPresent());
                if (!activeMute.isPresent()) {
                    muteService.getLastMute(onlinePlayer.get().getUniqueId())
                        .filter(m -> !m.isSoftMute())
                        .ifPresent(mute -> messages.send(onlinePlayer.get().getPlayer(), messages.muteExpired, messages.prefixGeneral));
                }
            }
        });
    }

    @NotNull
    private Optional<Player> getPlayer(ArrayList<Player> onlinePlayers, OnlinePlayerSession mutedSession) {
        return onlinePlayers.stream().filter(p -> p.getUniqueId() == mutedSession.getUuid()).findFirst();
    }
}