package net.shortninja.staffplus.core.domain.staff.mute;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
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

public class MuteSessionTask extends BukkitRunnable {
    public static final int DELAY = 10 * 20;
    private final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private final Messages messages = IocContainer.get(Messages.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final MuteService muteService = IocContainer.get(MuteService.class);

    public MuteSessionTask() {
        runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
    }

    @Override
    public void run() {
        if(!IocContainer.get(Options.class).muteConfiguration.isEnabled()) {
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