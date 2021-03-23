package net.shortninja.staffplus.core.domain.staff.mute;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean
public class MuteSessionTask extends BukkitRunnable {
    public static final int DELAY = 10 * 20;
    private final MessageCoordinator message;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final MuteService muteService;
    private final Options options;

    public MuteSessionTask(MessageCoordinator message, Messages messages, SessionManagerImpl sessionManager, MuteService muteService, Options options) {
        this.message = message;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.muteService = muteService;
        this.options = options;
        runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
    }

    @Override
    public void run() {
        if(!options.muteConfiguration.isEnabled()) {
            return;
        }
        List<Player> players = sessionManager.getAll().stream()
            .filter(p -> p.getPlayer().isPresent())
            .map(p -> p.getPlayer().get())
            .collect(Collectors.toList());

        List<Mute> activeMutes = muteService.getAllActiveMutes(players);

        for (PlayerSession playerSession : sessionManager.getAll()) {
            Optional<Player> player = playerSession.getPlayer();
            if (playerSession.isMuted() && player.isPresent()) {
                boolean playerIsMuted = activeMutes.stream().anyMatch(mute -> mute.getTargetUuid().equals(player.get().getUniqueId()));
                playerSession.setMuted(playerIsMuted);
                if (!playerIsMuted) {
                    message.send(player.get(), messages.muteExpired, messages.prefixGeneral);
                }
            }
        }
    }
}