package net.shortninja.staffplus.core.domain.webui.config;

public class WebUiConfiguration {

    private final boolean enabled;
    private final String host;
    private final String registrationCmd;
    private final String registrationPermission;
    private final String applicationKey;
    private final String role;

    public WebUiConfiguration(boolean enabled, String host, String applicationKey, String registrationCmd, String registrationPermission, String role) {
        this.enabled = enabled;
        this.host = host;
        this.applicationKey = applicationKey;
        this.registrationCmd = registrationCmd;
        this.registrationPermission = registrationPermission;
        this.role = role;
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

    public String getApplicationKey() {
        return applicationKey;
    }

    public String getRole() {
        return role;
    }
}
