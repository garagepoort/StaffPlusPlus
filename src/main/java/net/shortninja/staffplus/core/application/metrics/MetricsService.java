package net.shortninja.staffplus.core.application.metrics;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;

@IocBean(conditionalOnProperty = "metrics=true")
public class MetricsService {

    private static final int PLUGIN_ID = 9351;

    public MetricsService(Options options, WarningConfiguration warningConfiguration, BanConfiguration banConfiguration, AltDetectConfiguration altDetectConfiguration, MuteConfiguration muteConfiguration) {
        Metrics metrics = new Metrics(StaffPlus.get(), PLUGIN_ID);

        boolean warningConfigurationEnabled = warningConfiguration.isEnabled();
        boolean appealConfigurationEnabled = options.appealConfiguration.enabled;
        boolean reportConfigurationEnabled = options.reportConfiguration.isEnabled();
        boolean banConfigurationEnabled = banConfiguration.enabled;
        boolean muteConfigurationEnabled = muteConfiguration.muteEnabled;
        boolean kickConfigurationEnabled = options.kickConfiguration.isEnabled();

        metrics.addCustomChart(new Metrics.SimplePie("warnings_module", () -> String.valueOf(warningConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("warning_appeals_module", () -> String.valueOf(appealConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("reports_module", () -> String.valueOf(reportConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("ban_module", () -> String.valueOf(banConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("mute_module", () -> String.valueOf(muteConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("kick_module", () -> String.valueOf(kickConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("alt_detection_module", () -> String.valueOf(altDetectConfiguration.enabled)));
    }
}
