package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.common.config.Options;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class StaffModePluginDisable implements PluginDisable {

    private final Options options;
    private final StaffModeService staffModeService;

    public StaffModePluginDisable(Options options, StaffModeService staffModeService) {
        this.options = options;
        this.staffModeService = staffModeService;
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        if (options.modeConfiguration.isModeDisableOnLogout()) {
            Bukkit.getOnlinePlayers().forEach(staffModeService::removeMode);
        }
    }
}
