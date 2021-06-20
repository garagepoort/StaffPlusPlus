package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

public class AltDetectConfiguration {

    private final boolean enabled;
    private final boolean sameIpRequired;
    private final String bypassPermission;
    private final String whitelistPermission;
    private final String commandWhitelist;


    public AltDetectConfiguration(boolean enabled, boolean sameIpRequired, String bypassPermission, String whitelistPermission, String commandWhitelist) {
        this.enabled = enabled;
        this.sameIpRequired = sameIpRequired;
        this.bypassPermission = bypassPermission;
        this.whitelistPermission = whitelistPermission;
        this.commandWhitelist = commandWhitelist;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getWhitelistPermission() {
        return whitelistPermission;
    }
    public String getBypassPermission() {
        return bypassPermission;
    }

    public String getCommandWhitelist() {
        return commandWhitelist;
    }

    public boolean isSameIpRequired() {
        return sameIpRequired;
    }
}
