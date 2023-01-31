package net.shortninja.staffplus.core.mode.listeners.bukkit;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.CommandUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiBuilder;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.mode.custommodules.CustomModuleHandler;
import net.shortninja.staffplus.core.mode.handler.CpsHandler;
import net.shortninja.staffplus.core.mode.handler.GadgetHandler;
import net.shortninja.staffplus.core.mode.handler.GadgetType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.shortninja.staffplus.core.freeze.FreezeHandler;
import net.shortninja.staffplus.core.freeze.FreezeRequest;

@IocBukkitListener
public class PlayerInteract implements Listener {

    private static final int COOLDOWN = 200;
    private static final Map<Player, Long> staffTimings = new HashMap<>();

    private final IProtocolService protocolService;
    private final CpsHandler cpsHandler;
    private final GadgetHandler gadgetHandler;
    private final FreezeHandler freezeHandler;
    private final PlayerManager playerManager;
    private final OnlineSessionsManager sessionManager;
    private final GuiActionService guiActionService;
    private final ChestGuiBuilder chestGuiBuilder;
    private final CommandUtil commandUtil;
    private final CustomModuleHandler customModuleHandler;


    public PlayerInteract(IProtocolService protocolService, CpsHandler cpsHandler,
                          GadgetHandler gadgetHandler,
                          FreezeHandler freezeHandler,
                          PlayerManager playerManager,
                          OnlineSessionsManager sessionManager,
                          GuiActionService guiActionService, ChestGuiBuilder chestGuiBuilder,
                          CommandUtil commandUtil, CustomModuleHandler customModuleHandler) {
        this.protocolService = protocolService;
        this.cpsHandler = cpsHandler;
        this.gadgetHandler = gadgetHandler;
        this.freezeHandler = freezeHandler;
        this.playerManager = playerManager;
        this.sessionManager = sessionManager;
        this.guiActionService = guiActionService;
        this.chestGuiBuilder = chestGuiBuilder;
        this.commandUtil = commandUtil;
        this.customModuleHandler = customModuleHandler;
    }

    @EventHandler(priority = EventPriority.LOWEST)
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

        Optional<GeneralModeConfiguration> modeConfig = playerSession.get("modeConfig");
        GeneralModeConfiguration modeConfiguration = modeConfig.get();
        if (staffCheckingChest(event, player)) {
            if (modeConfiguration.isModeSilentChestInteraction() && !player.isSneaking()) {

                Container container = (Container) event.getClickedBlock().getState();
                chestGuiBuilder.build(container, modeConfiguration.isModeSilentChestInteraction()).show(player);
                event.setCancelled(true);
            }
            return;
        }

        if (!playerSession.getCurrentGui().isPresent() && handleInteraction(player, item, action)) {
            event.setCancelled(true);
        }
    }

    private boolean staffCheckingChest(PlayerInteractEvent event, Player player) {
        return event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container
            && sessionManager.get(player).isInStaffMode();
    }

    private boolean handleInteraction(Player player, ItemStack item, Action action) {
        boolean isHandled = true;
        if (action == Action.PHYSICAL) {
            return false;
        }

        GadgetType gadgetType = gadgetHandler.getGadgetType(protocolService.getVersionProtocol().getNbtString(item));
        if (staffTimings.containsKey(player) && System.currentTimeMillis() - staffTimings.get(player) <= COOLDOWN) {
            //Still cooling down but cancel the event if it is a staff item
            return gadgetType != GadgetType.CUSTOM;
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
                commandUtil.playerAction(player, () -> {
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
                commandUtil.playerAction(player, () -> {
                    Player t = JavaUtils.getTargetPlayer(player);
                    if (t != null) {
                        guiActionService.executeAction(player, "players/view/detail?targetPlayerName=" + t.getName());
                    }
                });
                break;
            case CUSTOM:
                isHandled = customModuleHandler.handleCustomModule(player, item);
                break;
            default:
                break;
        }

        staffTimings.put(player, System.currentTimeMillis());
        return isHandled;
    }
}
