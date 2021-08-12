package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class StaffModePluginDisable implements PluginDisable {

    private final StaffModeService staffModeService;
    private final ModeProvider modeProvider;
    private final PlayerSettingsRepository playerSettingsRepository;

    public StaffModePluginDisable(StaffModeService staffModeService, ModeProvider modeProvider, PlayerSettingsRepository playerSettingsRepository) {
        this.staffModeService = staffModeService;
        this.modeProvider = modeProvider;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            try {
                PlayerSettings playerSettings = playerSettingsRepository.get(onlinePlayer);
                if (playerSettings.isInStaffMode()) {
                    GeneralModeConfiguration mode = modeProvider.getMode(onlinePlayer, playerSettings.getModeName().get());
                    if (mode.isModeDisableOnLogout()) {
                        staffModeService.turnStaffModeOff(onlinePlayer);
                    }
                }
            } catch (Exception e) {
                StaffPlus.get().getLogger().warning("Enable to execute staff mode disable for user: [" + onlinePlayer.getName() + "]");
            }
        }
    }
}
