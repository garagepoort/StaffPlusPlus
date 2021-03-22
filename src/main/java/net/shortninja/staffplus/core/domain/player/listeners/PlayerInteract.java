package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGUI;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiType;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeRequest;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CpsHandler;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CustomModuleExecutor;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CustomModulePreProcessor;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static net.shortninja.staffplus.core.common.cmd.CommandUtil.playerAction;
import static net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration.ModuleType.COMMAND_DYNAMIC;

public class PlayerInteract implements Listener {

    private static final int COOLDOWN = 200;
    private static final Map<Player, Long> staffTimings = new HashMap<>();

    private final IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private final CpsHandler cpsHandler = StaffPlus.get().cpsHandler;
    private final GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;
    private final FreezeHandler freezeHandler = IocContainer.get(FreezeHandler.class);
    private final PlayerManager playerManager = IocContainer.get(PlayerManager.class);
    private final Options options = IocContainer.get(Options.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final List<CustomModulePreProcessor> customModulePreProcessors = IocContainer.getList(CustomModulePreProcessor.class);
    private final Messages messages = IocContainer.get(Messages.class);
    private final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);

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
        }

        boolean inStaffMode = sessionManager.get(uuid).isInStaffMode();

        if (!inStaffMode || item == null) {
            return;
        }

        if (staffCheckingChest(event, player)) {
            event.setCancelled(true);
            Container container = (Container) event.getClickedBlock().getState();

            if (container instanceof Furnace) {
                new ChestGUI(container.getInventory(), InventoryType.FURNACE, ChestGuiType.CONTAINER, options.modeConfiguration.isModeSilentChestInteraction()).show(player);
            } else if (container instanceof BrewingStand) {
                new ChestGUI(container.getInventory(), InventoryType.BREWING, ChestGuiType.CONTAINER, options.modeConfiguration.isModeSilentChestInteraction()).show(player);
            } else if (container instanceof Dispenser || container instanceof Dropper) {
                new ChestGUI(container.getInventory(), InventoryType.DISPENSER, ChestGuiType.CONTAINER, options.modeConfiguration.isModeSilentChestInteraction()).show(player);
            } else if (container instanceof Hopper) {
                new ChestGUI(container.getInventory(), InventoryType.HOPPER, ChestGuiType.CONTAINER, options.modeConfiguration.isModeSilentChestInteraction()).show(player);
            } else {
                // Either Chest, Chest-like or new block.
                // If it's a non-standard size for some reason, make it work with chests naively and show it. - Will produce errors with onClose() tho.
                int containerSize = container.getInventory().getSize();
                if (containerSize % 9 != 0) {
                    Bukkit.getLogger().warning("Non-standard container, expecting an exception below.");
                    containerSize += (9 - containerSize % 9);
                }
                new ChestGUI(container.getInventory(), containerSize, ChestGuiType.CONTAINER, options.modeConfiguration.isModeSilentChestInteraction()).show(player);
            }
            return;
        }

        if (inStaffMode) {
            if (handleInteraction(player, item, action)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean staffCheckingChest(PlayerInteractEvent event, Player player) {
        return event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container
            && sessionManager.get(player.getUniqueId()).isInStaffMode()
            && !player.isSneaking();
    }

    private boolean handleInteraction(Player player, ItemStack item, Action action) {
        boolean isHandled = true;
        if (action == Action.PHYSICAL) {
            return false;
        }

        GadgetHandler.GadgetType gadgetType = gadgetHandler.getGadgetType(item, versionProtocol.getNbtString(item));
        if (staffTimings.containsKey(player)) {
            if (System.currentTimeMillis() - staffTimings.get(player) <= COOLDOWN) {
                //Still cooling down but cancel the event if it is a staff item
                return gadgetType != GadgetHandler.GadgetType.CUSTOM;
            }
        }

        switch (gadgetType) {
            case COMPASS:
                gadgetHandler.onCompass(player);
                break;
            case RANDOM_TELEPORT:
                gadgetHandler.onRandomTeleport(player);
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
                    if (targetPlayer != null) {
                        freezeHandler.execute(new FreezeRequest(player, targetPlayer, !freezeHandler.isFrozen(targetPlayer.getUniqueId())));
                    }
                });
                break;
            case CPS:
                gadgetHandler.onCps(player, JavaUtils.getTargetPlayer(player));
                break;
            case EXAMINE:
                Player targetPlayer = JavaUtils.getTargetPlayer(player);
                if (targetPlayer == null) {
                    break;
                }

                Optional<SppPlayer> onlinePlayer = playerManager.getOnlinePlayer(targetPlayer.getUniqueId());
                gadgetHandler.onExamine(player, onlinePlayer.get());
                break;
            case FOLLOW:
                gadgetHandler.onFollow(player, JavaUtils.getTargetPlayer(player));
                break;
            case CUSTOM:
                isHandled = handleCustomModule(player, item);
                break;
        }

        staffTimings.put(player, System.currentTimeMillis());
        return isHandled;
    }

    private boolean handleCustomModule(Player player, ItemStack item) {
        Optional<CustomModuleConfiguration> customModuleConfiguration = gadgetHandler.getModule(item);
        if (!customModuleConfiguration.isPresent()) {
            return false;
        }

        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%clicker%", player.getName());
        Player targetPlayer = JavaUtils.getTargetPlayer(player);
        if (targetPlayer != null) {
            placeholders.put("%clicked%", targetPlayer.getName());
        }

        if(customModuleConfiguration.get().getType() == COMMAND_DYNAMIC && targetPlayer == null) {
            message.send(player, "No target in range", messages.prefixGeneral);
            return true;
        }

        CustomModuleExecutor moduleExecution = (p, pl) -> gadgetHandler.executeModule(p, targetPlayer, customModuleConfiguration.get(), pl);
        for (CustomModulePreProcessor customModulePreProcessor : customModulePreProcessors) {
            moduleExecution = customModulePreProcessor.process(moduleExecution, customModuleConfiguration.get(), placeholders);
        }
        moduleExecution.execute(player, placeholders);
        return true;
    }
}
