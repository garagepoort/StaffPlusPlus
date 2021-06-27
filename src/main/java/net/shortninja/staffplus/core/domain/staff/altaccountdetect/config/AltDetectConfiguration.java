package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class AltDetectConfiguration {

    @ConfigProperty("alt-detect-module.enabled")
    public boolean enabled;
    @ConfigProperty("alt-detect-module.same-ip-required")
    public boolean sameIpRequired;

    @ConfigProperty("permissions:permissions.alt-detect-bypass")
    public String bypassPermission;
    @ConfigProperty("permissions:permissions.alt-detect-whitelist")
    public String whitelistPermission;
    @ConfigProperty("permissions:permissions.alt-detect-check")
    public String checkPermission;

    @ConfigProperty("commands:commands.alt-detect-whitelist")
    public String commandWhitelist;
    @ConfigProperty("commands:commands.alt-detect-check")
    public String commandCheck;
}
