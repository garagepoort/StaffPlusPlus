package net.shortninja.staffplus.core.domain.webui.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class WebUiConfiguration {

    @ConfigProperty("webui-module.enabled")
    public boolean enabled;
    @ConfigProperty("webui-module.host")
    public String host;
    @ConfigProperty("webui-module.application-key")
    public String applicationKey;
    @ConfigProperty("webui-module.role")
    public String role;

    @ConfigProperty("commands:webui.register")
    public String registrationCmd;
    @ConfigProperty("permissions:webui.register")
    public String registrationPermission;

}
