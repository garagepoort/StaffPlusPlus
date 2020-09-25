package net.shortninja.staffplus.staff.reporting.config;

import net.shortninja.staffplus.common.ConfigLoader;
import net.shortninja.staffplus.util.lib.Sounds;

public class ReportingModuleLoader extends ConfigLoader<ReportConfiguration> {

    @Override
    public ReportConfiguration load() {
        boolean enabled = config.getBoolean("reports-module.enabled");
        int cooldown = config.getInt("reports-module.cooldown");
        boolean showReporter = config.getBoolean("reports-module.show-reporter");
        boolean closingReasonEnabled = config.getBoolean("reports-module.closing-reason-enabled", true);
        Sounds sound = stringToSound(sanitize(config.getString("reports-module.sound", "NONE")));

        return new ReportConfiguration(enabled, cooldown, showReporter, sound, closingReasonEnabled);
    }
}
