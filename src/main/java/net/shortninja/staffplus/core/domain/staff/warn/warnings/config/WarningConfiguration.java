package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.SoundsConfigTransformer;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.domain.actions.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ConfiguredCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@IocBean
public class WarningConfiguration {

    @ConfigProperty("warnings-module.enabled")
    private boolean enabled;
    @ConfigProperty("warnings-module.show-issuer")
    private boolean showIssuer;
    @ConfigProperty("warnings-module.sound")
    @ConfigTransformer(SoundsConfigTransformer.class)
    private Sounds sound;
    @ConfigProperty("warnings-module.user-notifications.enabled")
    private boolean notifyUser;
    @ConfigProperty("warnings-module.user-notifications.always-notify")
    private boolean alwaysNotifyUser;
    @ConfigProperty("warnings-module.thresholds")
    @ConfigTransformer(ThresholdConfigTransformer.class)
    private List<WarningThresholdConfiguration> thresholds = new ArrayList<>();
    @ConfigProperty("warnings-module.severity-levels")
    @ConfigTransformer(SeverityConfigTransformer.class)
    private List<WarningSeverityConfiguration> severityLevels = new ArrayList<>();
    @ConfigProperty("warnings-module.actions")
    @ConfigTransformer(ActionConfigLoader.class)
    private List<ConfiguredCommand> actions = new ArrayList<>();

    @ConfigProperty("commands:my-warnings")
    private String myWarningsCmd;
    @ConfigProperty("permissions:view-my-warnings")
    private String myWarningsPermission;
    @ConfigProperty("permissions:warnings.notifications")
    private String notificationsPermission;

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isShowIssuer() {
        return showIssuer;
    }

    public Optional<Sounds> getSound() {
        return Optional.ofNullable(sound);
    }

    public boolean isAlwaysNotifyUser() {
        return alwaysNotifyUser;
    }

    public List<WarningThresholdConfiguration> getThresholds() {
        return thresholds;
    }

    public List<WarningSeverityConfiguration> getSeverityLevels() {
        return severityLevels;
    }

    public boolean isNotifyUser() {
        return notifyUser;
    }

    public String getMyWarningsPermission() {
        return myWarningsPermission;
    }

    public String getMyWarningsCmd() {
        return myWarningsCmd;
    }

    public List<ConfiguredCommand> getActions() {
        return actions;
    }

    public String getNotificationsPermission() {
        return notificationsPermission;
    }

    public Optional<WarningSeverityConfiguration> getSeverityConfiguration(String severityLevel) {
        return severityLevels.stream().filter(config -> config.getName().equalsIgnoreCase(severityLevel)).findFirst();
    }
}
