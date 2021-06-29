package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.cmd.CommandUtil;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PassThroughClickAction;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@IocBean
@IocListener
public class InventoryClick implements Listener {
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public InventoryClick(Options options, SessionManagerImpl sessionManager) {
        this.options = options;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        PlayerSession playerSession = sessionManager.get(uuid);
        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();

        if (!playerSession.getCurrentGui().isPresent() || item == null) {
            if (playerSession.isInStaffMode() && !playerSession.getModeConfiguration().get().isModeInventoryInteraction()) {
                event.setCancelled(true);
            }
            return;
        }

        if (event.getClickedInventory() != null && event.getClickedInventory().equals(playerSession.getCurrentGui().get().getInventory())) {
            IAction action = playerSession.getCurrentGui().get().getAction(slot);
            if (action != null) {
                if (action instanceof PassThroughClickAction) {
                    return;
                }

                CommandUtil.playerAction(player, () -> action.click(player, item, slot, event.getClick()));

                if (action.shouldClose(player)) {
                    player.closeInventory();
                }
            }
            event.setCancelled(true);
        }

    }
}