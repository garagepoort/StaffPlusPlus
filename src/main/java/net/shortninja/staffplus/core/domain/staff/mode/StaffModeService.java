package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.actions.PermissionActionFilter;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionLoader;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class StaffModeService {


    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final VanishServiceImpl vanishServiceImpl;
    private final StaffModeItemsService staffModeItemsService;
    private final ActionService actionService;

    private final ModeDataRepository modeDataRepository;
    private final Options options;
    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final SessionLoader sessionLoader;

    public StaffModeService(Options options,
                            Messages messages,
                            SessionManagerImpl sessionManager,
                            VanishServiceImpl vanishServiceImpl,
                            StaffModeItemsService staffModeItemsService,
                            ActionService actionService, ModeDataRepository modeDataRepository, PlayerManager playerManager, ModeProvider modeProvider, SessionLoader sessionLoader) {
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.vanishServiceImpl = vanishServiceImpl;
        this.options = options;
        this.actionService = actionService;
        this.playerManager = playerManager;
        this.staffModeItemsService = staffModeItemsService;
        this.modeDataRepository = modeDataRepository;
        this.modeProvider = modeProvider;
        this.sessionLoader = sessionLoader;
    }

    public Set<UUID> getModeUsers() {
        return sessionManager.getAll().stream()
            .filter(p -> p.getPlayer().isPresent() && p.getPlayer().get().isOnline())
            .map(PlayerSession::getUuid).collect(Collectors.toSet());
    }

    public void turnStaffModeOn(Player player, String mode) {
        GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, mode);

        turnStaffModeOn(player, modeConfiguration);
    }

    public void turnStaffModeOn(Player player) {
        GeneralModeConfiguration modeConfiguration = modeProvider.calculateMode(player)
            .orElseThrow(() -> new BusinessException("&CNo suitable staff mode found. Can't enable staff mode"));

       this.turnStaffModeOn(player, modeConfiguration);
    }

    public void turnStaffModeOn(Player player, GeneralModeConfiguration modeConfiguration) {
        UUID uuid = player.getUniqueId();
        PlayerSession session = sessionManager.get(uuid);

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            ModeData modeData = new ModeData(player, session.getVanishType());
            modeDataRepository.saveModeData(modeData);
        }
        staffModeItemsService.setStaffModeItems(player, modeConfiguration);

        player.setAllowFlight(modeConfiguration.isModeFlight());
        if (modeConfiguration.isModeCreative()) player.setGameMode(GameMode.CREATIVE);

        runModeCommands(player, true, modeConfiguration);
        vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());

        session.setInStaffMode(true);
        session.setModeConfiguration(modeConfiguration);
        sessionLoader.saveSession(session);
        sendEvent(new EnterStaffModeEvent(player.getName(), player.getUniqueId(), player.getLocation(), options.serverName));
        messages.send(player, messages.modeStatus.replace("%status%", messages.enabled), messages.prefixGeneral);
    }

    public void toggleStaffFly(Player player) {
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        if (!playerSession.isInStaffMode()) {
            throw new BusinessException("&CYou can only toggle fly while in staff mode");
        }

        Optional<GeneralModeConfiguration> modeConfiguration = playerSession.getModeConfiguration();
        if (modeConfiguration.isPresent() && !modeConfiguration.get().isModeFlight()) {
            throw new BusinessException("&CCannot toggle fly. Flight is not enabled while in staff mode");
        }
        player.setAllowFlight(!player.getAllowFlight());
    }

    public void turnStaffModeOff(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            return;
        }

        Optional<GeneralModeConfiguration> modeConfiguration = session.getModeConfiguration();
        if(modeConfiguration.isPresent()) {
            ModeData modeData = existingModeData.get();
            if (modeConfiguration.get().isModeOriginalLocation()) {
                player.teleport(modeData.getPreviousLocation().setDirection(player.getLocation().getDirection()));
                messages.send(player, messages.modeOriginalLocation, messages.prefixGeneral);
            }

            runModeCommands(player, false, modeConfiguration.get());
            JavaUtils.clearInventory(player);
            player.getInventory().setContents(modeData.getPlayerInventory());
            player.updateInventory();
            player.setExp(modeData.getXp());
            player.setAllowFlight(modeData.hasFlight());
            player.setGameMode(modeData.getGameMode());
            player.setFireTicks(modeData.getFireTicks());

            if (modeData.getVanishType() == VanishType.NONE) {
                vanishServiceImpl.removeVanish(player);
            } else {
                vanishServiceImpl.addVanish(player, modeData.getVanishType());
            }
            modeDataRepository.deleteModeData(player);
        }


        session.setInStaffMode(false);
        session.setModeConfiguration(null);
        sendEvent(new ExitStaffModeEvent(player.getName(), player.getUniqueId(), player.getLocation(), options.serverName));
        messages.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
    }

    private void runModeCommands(Player player, boolean isEnabled, GeneralModeConfiguration modeConfiguration) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(player.getUniqueId());
        if (target.isPresent()) {
            List<ActionFilter> actionFilters = Collections.singletonList(new PermissionActionFilter());
            List<ConfiguredAction> actions = isEnabled ? modeConfiguration.getModeEnableCommands() : modeConfiguration.getModeDisableCommands();
            actionService.executeActions(configuredAction -> target.get(), actions, actionFilters, new HashMap<>());
        }
    }

    public static ItemStack[] getContents(Player p) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i <= 35; i++) {
            itemStacks.add(p.getInventory().getItem(i));
        }
        return itemStacks.toArray(new ItemStack[]{});
    }

    public Optional<GeneralModeConfiguration> getModeConfig(Player player) {
        return modeProvider.calculateMode(player);
    }
}