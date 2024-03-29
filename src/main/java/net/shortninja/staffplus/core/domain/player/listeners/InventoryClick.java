package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.CommandUtil;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PassThroughClickAction;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@IocBukkitListener
public class InventoryClick implements Listener {
    private final OnlineSessionsManager sessionManager;
    private final CommandUtil commandUtil;
    private final StaffModeItemsService staffModeItemsService;

    public InventoryClick(OnlineSessionsManager sessionManager, CommandUtil commandUtil, StaffModeItemsService staffModeItemsService) {
        this.sessionManager = sessionManager;
        this.commandUtil = commandUtil;
        this.staffModeItemsService = staffModeItemsService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        OnlinePlayerSession playerSession = sessionManager.get(player);
        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();

        if (!playerSession.getCurrentGui().isPresent() || item == null) {
            if (playerSession.isInStaffMode()) {
                if (!playerSession.getModeConfig().get().isModeInventoryInteraction()) {
                    event.setCancelled(true);
                    return;
                }
                Optional<? extends ModeItemConfiguration> module = staffModeItemsService.getModule(item);
                if (module.isPresent() && !module.get().isMovable()) {
                    event.setCancelled(true);
                    return;
                }
            }
            return;
        }

        if (event.getClickedInventory() != null && event.getClickedInventory().equals(playerSession.getCurrentGui().get().getInventory())) {
            IAction action = playerSession.getCurrentGui().get().getAction(slot);
            if (action != null) {
                if (action instanceof PassThroughClickAction) {
                    return;
                }

                commandUtil.playerAction(player, () -> action.click(player, item, slot, event.getClick()));

                if (action.shouldClose(player)) {
                    player.closeInventory();
                }
            }
            event.setCancelled(true);
        }
    }
}