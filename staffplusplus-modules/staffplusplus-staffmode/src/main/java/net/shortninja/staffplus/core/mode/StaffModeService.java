package net.shortninja.staffplus.core.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEventOnThisTick;

@IocBean
public class StaffModeService {

    private static final String MODE_CONFIG = "modeConfig";
    private final PlayerSettingsRepository playerSettingsRepository;
    private final ModeDataRepository modeDataRepository;
    private final Options options;
    private final ModeProvider modeProvider;

    public StaffModeService(Options options,
                            PlayerSettingsRepository playerSettingsRepository,
                            ModeDataRepository modeDataRepository,
                            ModeProvider modeProvider) {
        this.playerSettingsRepository = playerSettingsRepository;
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

        playerSettings.setInStaffMode(true);
        playerSettings.setModeName(modeConfiguration.getName());
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

        settings.setInStaffMode(true);
        settings.setModeName(modeConfiguration.getName());
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
        PlayerSettings settings = playerSettingsRepository.get(player);
        settings.setInStaffMode(false);
        settings.setModeName(null);
        playerSettingsRepository.save(settings);

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            return;
        }
        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(settings.getModeName().orElse(null));

        modeDataRepository.deleteModeData(player);
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

    public void turnStaffModeOffOnQuit(Player player) {

        PlayerSettings settings = playerSettingsRepository.get(player);
        settings.setInStaffMode(false);
        settings.setModeName(null);
        playerSettingsRepository.save(settings);

        Optional<ModeData> existingModeData = modeDataRepository.retrieveModeData(player.getUniqueId());
        if (!existingModeData.isPresent()) {
            return;
        }
        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(settings.getModeName().orElse(null));

        modeDataRepository.deleteModeData(player);
        sendEventOnThisTick(new ExitStaffModeEvent(
            player.getName(),
            player.getUniqueId(),
            player.getLocation(),
            options.serverName,
            modeConfiguration.map(GeneralModeConfiguration::getName).orElse("default"),
            existingModeData.get(),
            null));
    }

    public Optional<GeneralModeConfiguration> getModeConfig(Player player) {
        return modeProvider.calculateMode(player);
    }
}