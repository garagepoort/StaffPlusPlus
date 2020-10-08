package net.shortninja.staffplus.authentication;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import org.bukkit.Bukkit;

import static net.shortninja.staffplus.authentication.AuthenticationProvider.AUTHME;

public class AuthenticationConfigurationLoader extends ConfigLoader<AuthenticationConfiguration> {

    @Override
    public AuthenticationConfiguration load() {
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
