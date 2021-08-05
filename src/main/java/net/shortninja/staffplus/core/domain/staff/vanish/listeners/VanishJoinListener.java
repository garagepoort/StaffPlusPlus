package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@IocBean
@IocListener
public class VanishJoinListener implements Listener {

    private final SessionManagerImpl sessionManager;
    private final VanishServiceImpl vanishServiceImpl;

    public VanishJoinListener(SessionManagerImpl sessionManager, VanishServiceImpl vanishServiceImpl) {
        this.sessionManager = sessionManager;
        this.vanishServiceImpl = vanishServiceImpl;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hideJoinMessage(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        PlayerSession session = sessionManager.get(player.getUniqueId());
        vanishServiceImpl.updateVanish(player);
        if (session.isVanished()) {
            playerJoinEvent.setJoinMessage("");
        }
    }
}
