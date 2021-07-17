package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class StaffModeService {


    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final VanishServiceImpl vanishServiceImpl;
    private final StaffModeItemsService staffModeItemsService;

    private final ModeDataRepository modeDataRepository;
    private final Options options;
    private final ModeProvider modeProvider;

    public StaffModeService(Options options,
                            Messages messages,
                            SessionManagerImpl sessionManager,
                            VanishServiceImpl vanishServiceImpl,
                            StaffModeItemsService staffModeItemsService,
                            ModeDataRepository modeDataRepository,
                            ModeProvider modeProvider) {
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.vanishServiceImpl = vanishServiceImpl;
        this.options = options;
        this.staffModeItemsService = staffModeItemsService;
        this.modeDataRepository = modeDataRepository;
        this.modeProvider = modeProvider;
    }

    public Set<UUID> getModeUsers() {
        return sessionManager.getAll().stream()
            .filter(p -> p.getPlayer().isPresent() && p.getPlayer().get().isOnline())
            .map(PlayerSession::getUuid).collect(Collectors.toSet());
    }

    public void turnStaffModeOn(Player player, String mode) {
        GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, mode);
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.isInStaffMode()) {
            this.switchStaffMode(player, modeConfiguration);
        } else {
            this.turnStaffModeOn(player, modeConfiguration);
        }
    }

    public void turnStaffModeOn(Player player) {
        GeneralModeConfiguration modeConfiguration = modeProvider.calculateMode(player)
            .orElseThrow(() -> new BusinessException("&CNo suitable staff mode found. Can't enable staff mode"));

        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.isInStaffMode()) {
            this.switchStaffMode(player, modeConfiguration);
        } else {
            this.turnStaffModeOn(player, modeConfiguration);
        }
    }

    private void switchStaffMode(Player player, GeneralModeConfiguration modeConfiguration) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            return;
        }

        resetPlayer(player, existingModeData.get());

        staffModeItemsService.setStaffModeItems(player, modeConfiguration);
        player.setAllowFlight(modeConfiguration.isModeFlight());
        if (modeConfiguration.isModeCreative()) {
            player.setGameMode(GameMode.CREATIVE);
        }

        vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());

        String fromMode = session.getModeConfiguration().get().getName();
        String toMode = modeConfiguration.getName();

        session.setModeConfiguration(modeConfiguration);
        sendEvent(new SwitchStaffModeEvent(player.getName(), player.getUniqueId(), player.getLocation(), options.serverName, fromMode, toMode));

        messages.send(player, "&eYou switched to staff mode &6" + modeConfiguration.getName(), messages.prefixGeneral);
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
        if (modeConfiguration.isModeCreative()) {
            player.setGameMode(GameMode.CREATIVE);
        }

        vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());

        session.setInStaffMode(true);
        session.setModeConfiguration(modeConfiguration);
        sendEvent(new EnterStaffModeEvent(player.getName(), player.getUniqueId(), player.getLocation(), options.serverName, modeConfiguration.getName()));
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
        String modeName = null;
        if (modeConfiguration.isPresent()) {
            ModeData modeData = existingModeData.get();
            if (modeConfiguration.get().isModeOriginalLocation()) {
                player.teleport(modeData.getPreviousLocation().setDirection(player.getLocation().getDirection()));
                messages.send(player, messages.modeOriginalLocation, messages.prefixGeneral);
            }

            resetPlayer(player, modeData);

            vanishServiceImpl.removeVanish(player);
            modeDataRepository.deleteModeData(player);
            modeName = modeConfiguration.get().getName();
        }


        session.setInStaffMode(false);
        session.setModeConfiguration(null);
        sendEvent(new ExitStaffModeEvent(player.getName(), player.getUniqueId(), player.getLocation(), options.serverName, modeName));
        messages.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
    }

    private void resetPlayer(Player player, ModeData modeData) {
        JavaUtils.clearInventory(player);
        player.getInventory().setContents(modeData.getPlayerInventory());
        player.updateInventory();
        player.setExp(modeData.getXp());
        player.setAllowFlight(modeData.hasFlight());
        player.setGameMode(modeData.getGameMode());
        player.setFireTicks(modeData.getFireTicks());
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