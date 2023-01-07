package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class AltDetectConfiguration {

    @ConfigProperty("alt-detect-module.enabled")
    public boolean enabled;
    @ConfigProperty("alt-detect-module.same-ip-required")
    public boolean sameIpRequired;

    @ConfigProperty("permissions:alt-detect-bypass")
    public String bypassPermission;
    @ConfigProperty("permissions:alt-detect-whitelist")
    public String whitelistPermission;

}
