package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class StaffModeService {


    private final PlayerSettingsRepository playerSettingsRepository;
    private final VanishServiceImpl vanishServiceImpl;
    private final ModeDataRepository modeDataRepository;
    private final Options options;
    private final ModeProvider modeProvider;

    public StaffModeService(Options options,
                            PlayerSettingsRepository playerSettingsRepository, VanishServiceImpl vanishServiceImpl,
                            ModeDataRepository modeDataRepository,
                            ModeProvider modeProvider) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.vanishServiceImpl = vanishServiceImpl;
        this.options = options;
        this.modeDataRepository = modeDataRepository;
        this.modeProvider = modeProvider;
    }

    public void turnStaffModeOn(Player player, String mode) {
        GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, mode);
        PlayerSettings session = playerSettingsRepository.get(player);
        if (session.isInStaffMode()) {
            this.switchStaffMode(player, modeConfiguration);
        } else {
            this.turnStaffModeOn(player, modeConfiguration);
        }
    }

    public void turnStaffModeOn(Player player) {
        GeneralModeConfiguration modeConfiguration = modeProvider.calculateMode(player)
            .orElseThrow(() -> new BusinessException("&CNo suitable staff mode found. Can't enable staff mode"));

        PlayerSettings session = playerSettingsRepository.get(player);
        if (session.isInStaffMode()) {
            this.switchStaffMode(player, modeConfiguration);
        } else {
            this.turnStaffModeOn(player, modeConfiguration);
        }
    }

    private void switchStaffMode(Player player, GeneralModeConfiguration modeConfiguration) {
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            return;
        }

        vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());
        playerSettings.setModeConfiguration(modeConfiguration);
        playerSettingsRepository.save(playerSettings);

        sendEvent(new SwitchStaffModeEvent(player.getName(),
            player.getUniqueId(),
            player.getLocation(),
            options.serverName,
            playerSettings.getName(),
            modeConfiguration.getName(),
            existingModeData.get()));
    }

    public void turnStaffModeOn(Player player, GeneralModeConfiguration modeConfiguration) {
        PlayerSettings settings = playerSettingsRepository.get(player);

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            ModeData modeData = new ModeData(player, settings.getVanishType());
            modeDataRepository.saveModeData(modeData);
        }

        vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());
        settings.setInStaffMode(true);
        settings.setModeConfiguration(modeConfiguration);
        playerSettingsRepository.save(settings);

        sendEvent(new EnterStaffModeEvent(player.getName(), player.getUniqueId(), player.getLocation(), options.serverName, modeConfiguration.getName()));
    }

    public void toggleStaffFly(Player player) {
        PlayerSettings playerSession = playerSettingsRepository.get(player);
        if (!playerSession.isInStaffMode()) {
            throw new BusinessException("&CYou can only toggle fly while in staff mode");
        }

        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(playerSession.getModeName().orElse(null));
        if (modeConfiguration.isPresent() && !modeConfiguration.get().isModeFlight()) {
            throw new BusinessException("&CCannot toggle fly. Flight is not enabled while in staff mode");
        }
        player.setAllowFlight(!player.getAllowFlight());
    }

    public void turnStaffModeOffAndTeleport(Player player, Location location) {
        PlayerSettings session = playerSettingsRepository.get(player);

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            return;
        }

        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(session.getModeName().orElse(null));

        session.setInStaffMode(false);
        session.setModeName(null);
        playerSettingsRepository.save(session);
        vanishServiceImpl.removeVanish(player);
        sendEvent(new ExitStaffModeEvent(
            player.getName(),
            player.getUniqueId(),
            player.getLocation(),
            options.serverName,
            modeConfiguration.map(GeneralModeConfiguration::getName).orElse("default"),
            existingModeData.get(),
            location));
    }

    public void turnStaffModeOff(Player player) {
        turnStaffModeOffAndTeleport(player, null);
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