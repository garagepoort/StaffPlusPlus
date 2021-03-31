package net.shortninja.staffplus.core.authentication;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import static net.shortninja.staffplus.core.authentication.AuthenticationProvider.AUTHME;

@IocBean
public class AuthenticationConfigurationLoader extends AbstractConfigLoader<AuthenticationConfiguration> {

    @Override
    protected AuthenticationConfiguration load(FileConfiguration config) {
        String authenticationProviderName = config.getString("authentication.provider", "NOOP");
        AuthenticationProvider authenticationProvider = AuthenticationProvider.valueOf(authenticationProviderName.toUpperCase());
        if(authenticationProvider == AUTHME) {
            if(Bukkit.getServer().getPluginManager().getPlugin("AuthMe") == null) {
                throw new BusinessException("&CIncorrect authentication configuration. AuthMe configured but plugin is not loaded");
            }
        }
        return new AuthenticationConfiguration(authenticationProvider);
    }
}
