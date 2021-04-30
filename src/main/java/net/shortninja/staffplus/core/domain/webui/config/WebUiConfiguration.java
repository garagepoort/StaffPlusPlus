package net.shortninja.staffplus.core.domain.webui.config;

public class WebUiConfiguration {

    private final boolean enabled;
    private final String host;
    private final String registrationCmd;
    private final String registrationPermission;

    public WebUiConfiguration(boolean enabled, String host, String registrationCmd, String registrationPermission) {
        this.enabled = enabled;
        this.host = host;
        this.registrationCmd = registrationCmd;
        this.registrationPermission = registrationPermission;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getHost() {
        return host;
    }

    public String getRegistrationCmd() {
        return registrationCmd;
    }

    public String getRegistrationPermission() {
        return registrationPermission;
    }
}
