package net.shortninja.staffplus.server.listener.player;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.freeze.FreezeRequest;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.mode.handler.CpsHandler;
import net.shortninja.staffplus.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.staff.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static net.shortninja.staffplus.common.cmd.CommandUtil.playerAction;

public class PlayerInteract implements Listener {
    private final IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private final ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private final CpsHandler cpsHandler = StaffPlus.get().cpsHandler;
    private final GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;
    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();

    public PlayerInteract() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Action action = event.getAction();
        ItemStack item = player.getInventory().getItemInMainHand();


        if (cpsHandler.isTesting(uuid) && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            cpsHandler.updateCount(uuid);
            return;
        }

        if (!modeCoordinator.isInMode(uuid) || item == null) {
            return;
        }
        if (modeCoordinator.isInMode(uuid)) {
            if (handleInteraction(player, item, action)) {
                event.setCancelled(true);
            }
        }
        if(event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Container
                    && StaffPlus.get().modeCoordinator.isInMode(event.getPlayer().getUniqueId())
                    && !player.isSneaking()) {
                event.setCancelled(true);
                Container container = (Container) event.getClickedBlock().getState();

                Inventory chestView = Bukkit.createInventory(player, InventoryType.CHEST);
                // Only have to check for similar inventory types as the items will map.
                if (container instanceof Furnace) {
                    chestView = Bukkit.createInventory(player, InventoryType.FURNACE);
                } else if (container instanceof BrewingStand) {
                    chestView = Bukkit.createInventory(player, InventoryType.BREWING);
                } else if (container instanceof Dispenser || container instanceof Dropper) {
                    chestView = Bukkit.createInventory(player, InventoryType.DISPENSER);
                } else if (container instanceof Hopper) {
                    chestView = Bukkit.createInventory(player, InventoryType.HOPPER);
                } else {
                    // Either Chest, Chest-like or new block.
                    // If it's a non-standard size for some reason, make it work with chests naively and show it. - Will produce errors with onClose() tho.
                    int containerSize = container.getInventory().getSize();
                    if (containerSize % 9 != 0) {
                        Bukkit.getLogger().warning("Non-standard container, expecting an exception below.");
                        containerSize += (9 - containerSize % 9);
                    }
                    chestView = Bukkit.createInventory(player, containerSize);
                }

                chestView.setContents(container.getInventory().getContents());
                event.getPlayer().openInventory(chestView);
                StaffPlus.get().viewedChest.add(chestView);
                StaffPlus.get().inventoryHandler.addVirtualUser(player.getUniqueId());
            }
        }
    }

    private boolean handleInteraction(Player player, ItemStack item, Action action) {
        boolean isHandled = true;

        if (!action.toString().contains("CLICK_AIR")) {
            return false;
        }

        switch (gadgetHandler.getGadgetType(item, versionProtocol.getNbtString(item))) {
            case COMPASS:
                gadgetHandler.onCompass(player);
                break;
            case RANDOM_TELEPORT:
                gadgetHandler.onRandomTeleport(player, 1);
                break;
            case VANISH:
                gadgetHandler.onVanish(player, true);
                break;
            case GUI_HUB:
                gadgetHandler.onGuiHub(player);
                break;
            case COUNTER:
                gadgetHandler.onCounter(player);
                break;
            case FREEZE:
                playerAction(player, () -> {
                    Player targetPlayer = JavaUtils.getTargetPlayer(player);
                    if(targetPlayer != null){
                        freezeHandler.execute(new FreezeRequest(player, targetPlayer, freezeHandler.isFrozen(targetPlayer.getUniqueId())));
                    }
                });
                break;
            case CPS:
                gadgetHandler.onCps(player, JavaUtils.getTargetPlayer(player));
                break;
            case EXAMINE:
                gadgetHandler.onExamine(player, JavaUtils.getTargetPlayer(player));
                break;
            case FOLLOW:
                gadgetHandler.onFollow(player, JavaUtils.getTargetPlayer(player));
                break;
            case CUSTOM:
                ModuleConfiguration moduleConfiguration = gadgetHandler.getModule(item);

                if (moduleConfiguration != null) {
                    gadgetHandler.onCustom(player, JavaUtils.getTargetPlayer(player), moduleConfiguration);
                } else {
                    isHandled = false;
                }
                break;
        }

        return isHandled;
    }
}
