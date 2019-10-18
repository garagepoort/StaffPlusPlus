package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.CpsHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.GadgetHandler;
import net.shortninja.staffplus.player.attribute.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerInteract implements Listener {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private CpsHandler cpsHandler = StaffPlus.get().cpsHandler;
    private GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;

    public PlayerInteract() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST )
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Action action = event.getAction();
        ItemStack item = player.getItemInHand();


        if (cpsHandler.isTesting(uuid) && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            cpsHandler.updateCount(uuid);
            return;
        }

        if (!modeCoordinator.isInMode(uuid) || item == null) {
            return;
        }
        if(modeCoordinator.isInMode(uuid)) {
            if (handleInteraction(player, item, action)) {
                event.setCancelled(true);
            }
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (StaffPlus.get().twelvePlus) {

                if (event.getClickedBlock().getState() instanceof Container
                        && StaffPlus.get().modeCoordinator.isInMode(event.getPlayer().getUniqueId())) {

                    event.setCancelled(true);
                    Container container = (Container) event.getClickedBlock().getState();
                    Inventory chestView = Bukkit.createInventory(event.getPlayer(), container.getInventory().getSize());
                    chestView.setContents(container.getInventory().getContents());
                    event.getPlayer().openInventory(chestView);
                    StaffPlus.get().viewedChest.put(chestView, event.getClickedBlock());
                    //StaffPlus.get().inventoryHandler.addVirtualUser();
                }
            } else {

                if (event.getClickedBlock().getState() instanceof Chest &&
                        StaffPlus.get().modeCoordinator.isInMode(event.getPlayer().getUniqueId())) {

                    Chest chest = (Chest) event.getClickedBlock().getState();
                    Inventory view = chest.getInventory();
                    Inventory chestView = Bukkit.createInventory(event.getPlayer(), view.getSize());
                    chestView.setContents(view.getContents());
                    event.getPlayer().openInventory(chestView);
                    StaffPlus.get().viewedChest.put(chestView, event.getClickedBlock());
                    //StaffPlus.get().inventoryHandler.addVirtualUser(p);
                }
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (event.getClickedBlock().getState() instanceof Container
                            && StaffPlus.get().modeCoordinator.isInMode(event.getPlayer().getUniqueId())
                            && player.isSneaking() == false) {
                        event.setCancelled(true);
                        Container container = (Container) event.getClickedBlock().getState();
                        Inventory chestView = Bukkit.createInventory(event.getPlayer(), container.getInventory().getType());
                        chestView.setContents(container.getInventory().getContents());
                        event.getPlayer().openInventory(chestView);
                        StaffPlus.get().viewedChest.put(chestView, event.getClickedBlock());
                        //StaffPlus.get().inventoryHandler.addVirtualUser(p);
                    }
                }
            }
        }//end of if click block
    }

    private boolean handleInteraction(Player player, ItemStack item, Action action) {
        boolean isHandled = true;

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return isHandled = false;
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
                gadgetHandler.onFreeze(player, JavaUtils.getTargetPlayer(player));
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
                } else isHandled = false;

                break;
        }

        return isHandled;
    }
}
