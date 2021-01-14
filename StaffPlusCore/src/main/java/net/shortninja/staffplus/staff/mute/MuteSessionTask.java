package net.shortninja.staffplus.staff.mute;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MuteSessionTask extends BukkitRunnable {
    public static final int DELAY = 10 * 20;
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final MuteService muteService = IocContainer.getMuteService();

    public MuteSessionTask() {
        runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
    }

    @Override
    public void run() {
        if(!IocContainer.getOptions().muteConfiguration.isEnabled()) {
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
                boolean playerIsMuted = activeMutes.stream().anyMatch(mute -> mute.getPlayerUuid().equals(player.get().getUniqueId()));
                playerSession.setMuted(playerIsMuted);
                if (!playerIsMuted) {
                    message.send(player.get(), messages.muteExpired, messages.prefixGeneral);
                }
            }
        }
    }
}