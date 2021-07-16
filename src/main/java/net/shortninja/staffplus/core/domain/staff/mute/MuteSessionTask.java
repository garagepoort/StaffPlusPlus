package net.shortninja.staffplus.core.domain.staff.mute;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean
public class MuteSessionTask extends BukkitRunnable {
    public static final int DELAY = 10 * 20;

    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final MuteService muteService;
    private final MuteConfiguration muteConfiguration;

    public MuteSessionTask(Messages messages, SessionManagerImpl sessionManager, MuteService muteService, MuteConfiguration muteConfiguration) {
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.muteService = muteService;
        this.muteConfiguration = muteConfiguration;
        runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
    }

    @Override
    public void run() {
        if(!muteConfiguration.muteEnabled) {
            return;
        }
        List<Player> players = sessionManager.getAll().stream()
            .filter(p -> p.getPlayer().isPresent())
            .map(p -> p.getPlayer().get())
            .collect(Collectors.toList());

        List<Mute> activeMutes = muteService.getAllActiveMutes(players);

        sessionManager.getAll().forEach(playerSession -> {
            Optional<Player> player = playerSession.getPlayer();
            if (playerSession.isMuted() && player.isPresent()) {
                boolean playerIsMuted = activeMutes.stream().anyMatch(mute -> mute.getTargetUuid().equals(player.get().getUniqueId()));
                playerSession.setMuted(playerIsMuted);
                if (!playerIsMuted) {
                    messages.send(player.get(), messages.muteExpired, messages.prefixGeneral);
                }
            }
        });
    }
}