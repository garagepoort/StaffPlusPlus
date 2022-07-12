package net.shortninja.staffplus.core.domain.staff.kick.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.List;

@IocBean
public class KickConfiguration {

    @ConfigProperty("kick-module.enabled")
    public boolean kickEnabled;

    @ConfigProperty("permissions:kick")
    public String permissionKickPlayer;

    @ConfigProperty("permissions:kick-bypass")
    public String permissionKickByPass;

    @ConfigProperty("kick-module.fixed-reason")
    public boolean fixedReason;

    @ConfigProperty("kick-module.reasons")
    @ConfigObjectList(KickReasonConfiguration.class)
    public List<KickReasonConfiguration> kickReasons;

}
