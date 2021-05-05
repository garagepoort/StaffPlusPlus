package net.shortninja.staffplus.core.domain.webui.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class WebUiModuleLoader extends AbstractConfigLoader<WebUiConfiguration> {

    @Override
    protected WebUiConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("webui-module.enabled");
        String host = defaultConfig.getString("webui-module.host");
        String applicationKey = defaultConfig.getString("webui-module.application-key");
        String registrationCmd = commandsConfig.getString("commands.webui.register");
        String registrationPermission = permissionsConfig.getString("permissions.webui.register");

        return new WebUiConfiguration(enabled, host, applicationKey, registrationCmd, registrationPermission);
    }
}
