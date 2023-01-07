package net.shortninja.staffplus.core.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.BeforeTubingReload;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(BeforeTubingReload.class)
public class StaffModePluginDisable implements BeforeTubingReload {

    private final StaffModeService staffModeService;
    private final ModeProvider modeProvider;
    private final PlayerSettingsRepository playerSettingsRepository;

    public StaffModePluginDisable(StaffModeService staffModeService, ModeProvider modeProvider, PlayerSettingsRepository playerSettingsRepository) {
        this.staffModeService = staffModeService;
        this.modeProvider = modeProvider;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @Override
    public void execute(TubingBukkitPlugin tubingPlugin) {
        disable();
    }

    private void disable() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            try {
                PlayerSettings playerSettings = playerSettingsRepository.get(onlinePlayer);
                if (playerSettings.isInStaffMode()) {
                    GeneralModeConfiguration mode = modeProvider.getMode(onlinePlayer, playerSettings.getModeName().get());
                    if (mode.isModeDisableOnLogout()) {
                        staffModeService.turnStaffModeOffOnQuit(onlinePlayer);
                    }
                }
            } catch (Exception e) {
                TubingBukkitPlugin.getPlugin().getLogger().warning("Enable to execute staff mode disable for user: [" + onlinePlayer.getName() + "]");
            }
        }
    }
}
