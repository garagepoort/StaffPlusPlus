package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGUI;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiType;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeRequest;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CpsHandler;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CustomModuleExecutor;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CustomModulePreProcessor;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetType;
import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.cmd.CommandUtil.playerAction;
import static net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration.ModuleType.COMMAND_DYNAMIC;

@IocBean
public class PlayerInteract implements Listener {

    private static final int COOLDOWN = 200;
    private static final Map<Player, Long> staffTimings = new HashMap<>();

    private final IProtocolService protocolService;
    private final CpsHandler cpsHandler;
    private final GadgetHandler gadgetHandler;
    private final FreezeHandler freezeHandler;
    private final PlayerManager playerManager;
    private final OnlineSessionsManager sessionManager;
    private final List<CustomModulePreProcessor> customModulePreProcessors;
    private final Messages messages;
    private final GuiActionService guiActionService;


    public PlayerInteract(IProtocolService protocolService, CpsHandler cpsHandler,
                          GadgetHandler gadgetHandler,
                          FreezeHandler freezeHandler,
                          PlayerManager playerManager,
                          OnlineSessionsManager sessionManager,
                          @IocMulti(CustomModulePreProcessor.class) List<CustomModulePreProcessor> customModulePreProcessors,
                          Messages messages, GuiActionService guiActionService) {
        this.protocolService = protocolService;
        this.cpsHandler = cpsHandler;
        this.gadgetHandler = gadgetHandler;
        this.freezeHandler = freezeHandler;
        this.playerManager = playerManager;
        this.sessionManager = sessionManager;
        this.customModulePreProcessors = customModulePreProcessors;
        this.messages = messages;
        this.guiActionService = guiActionService;

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

        OnlinePlayerSession playerSession = sessionManager.get(player);
        if (!playerSession.isInStaffMode() || item == null) {
            return;
        }

        GeneralModeConfiguration modeConfiguration = playerSession.getModeConfig().get();
        if (staffCheckingChest(event, player)) {
            ChestGUI chestGUI = getStaffChestGui(event, modeConfiguration);
            event.setCancelled(true);
            chestGUI.show(player);
            return;
        }

        if (!playerSession.getCurrentGui().isPresent()) {
            if (handleInteraction(player, item, action)) {
                event.setCancelled(true);
            }
        }
    }

    @NotNull
    private ChestGUI getStaffChestGui(PlayerInteractEvent event, GeneralModeConfiguration modeConfiguration) {
        Container container = (Container) event.getClickedBlock().getState();

        ChestGUI chestGUI;
        if (container instanceof Furnace) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.FURNACE, ChestGuiType.CONTAINER, modeConfiguration.isModeSilentChestInteraction());
        } else if (container instanceof BrewingStand) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.BREWING, ChestGuiType.CONTAINER, modeConfiguration.isModeSilentChestInteraction());
        } else if (container instanceof Dispenser || container instanceof Dropper) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.DISPENSER, ChestGuiType.CONTAINER, modeConfiguration.isModeSilentChestInteraction());
        } else if (container instanceof Hopper) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.HOPPER, ChestGuiType.CONTAINER, modeConfiguration.isModeSilentChestInteraction());
        } else {
            // Either Chest, Chest-like or new block.
            // If it's a non-standard size for some reason, make it work with chests naively and show it. - Will produce errors with onClose() tho.
            int containerSize = container.getInventory().getSize();
            if (containerSize % 9 != 0) {
                Bukkit.getLogger().warning("Non-standard container, expecting an exception below.");
                containerSize += (9 - containerSize % 9);
            }
            chestGUI = new ChestGUI(container.getInventory(), containerSize, ChestGuiType.CONTAINER, modeConfiguration.isModeSilentChestInteraction());
        }
        return chestGUI;
    }

    private boolean staffCheckingChest(PlayerInteractEvent event, Player player) {
        return event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container
            && sessionManager.get(player).isInStaffMode()
            && !player.isSneaking();
    }

    private boolean handleInteraction(Player player, ItemStack item, Action action) {
        boolean isHandled = true;
        if (action == Action.PHYSICAL) {
            return false;
        }

        GadgetType gadgetType = gadgetHandler.getGadgetType(protocolService.getVersionProtocol().getNbtString(item));
        if (staffTimings.containsKey(player)) {
            if (System.currentTimeMillis() - staffTimings.get(player) <= COOLDOWN) {
                //Still cooling down but cancel the event if it is a staff item
                return gadgetType != GadgetType.CUSTOM;
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
                gadgetHandler.onVanish(player);
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
                        OnlinePlayerSession session = sessionManager.get(targetPlayer);
                        freezeHandler.execute(new FreezeRequest(player, targetPlayer, !session.isFrozen()));
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
            case PLAYER_DETAILS:
                playerAction(player, () -> {
                    Player t = JavaUtils.getTargetPlayer(player);
                    if (t != null) {
                        guiActionService.executeAction(player, "players/view/detail?targetPlayerName=" + t.getName());
                    }
                });
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

        if (customModuleConfiguration.get().getType() == COMMAND_DYNAMIC && targetPlayer == null) {
            messages.send(player, "No target in range", messages.prefixGeneral);
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
