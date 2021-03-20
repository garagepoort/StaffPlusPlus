package net.shortninja.staffplus.application.metrics;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.Metrics;

public class MetricsService {

    private static final int PLUGIN_ID = 9351;
    private final Metrics metrics;
    private final Options options;

    public MetricsService(StaffPlus plugin, Options options) {
        metrics = new Metrics(plugin, PLUGIN_ID);
        this.options = options;
    }

    public void initializeMetrics() {
        boolean warningConfigurationEnabled = options.warningConfiguration.isEnabled();
        boolean appealConfigurationEnabled = options.appealConfiguration.isEnabled();
        boolean reportConfigurationEnabled = options.reportConfiguration.isEnabled();
        boolean banConfigurationEnabled = options.banConfiguration.isEnabled();
        boolean muteConfigurationEnabled = options.muteConfiguration.isEnabled();
        boolean kickConfigurationEnabled = options.kickConfiguration.isEnabled();
        boolean altDetectionConfigurationEnabled = options.altDetectConfiguration.isEnabled();

        metrics.addCustomChart(new Metrics.SimplePie("warnings_module", () -> String.valueOf(warningConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("warning_appeals_module", () -> String.valueOf(appealConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("reports_module", () -> String.valueOf(reportConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("ban_module", () -> String.valueOf(banConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("mute_module", () -> String.valueOf(muteConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("kick_module", () -> String.valueOf(kickConfigurationEnabled)));
        metrics.addCustomChart(new Metrics.SimplePie("alt_detection_module", () -> String.valueOf(altDetectionConfigurationEnabled)));
    }
}
