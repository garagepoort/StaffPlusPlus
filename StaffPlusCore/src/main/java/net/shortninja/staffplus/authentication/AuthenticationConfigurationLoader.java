package net.shortninja.staffplus.authentication;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import static net.shortninja.staffplus.authentication.AuthenticationProvider.AUTHME;

public class AuthenticationConfigurationLoader {

    private final static FileConfiguration config = StaffPlus.get().getConfig();

    public static AuthenticationConfiguration load() {
        String authenticationProviderName = config.getString("authentication.provider", "NOOP");
        AuthenticationProvider authenticationProvider = AuthenticationProvider.valueOf(authenticationProviderName.toUpperCase());
        if(authenticationProvider == AUTHME) {
            if(Bukkit.getServer().getPluginManager().getPlugin("AuthMe") == null) {
                throw new BusinessException("Incorrect authentication configuration. AuthMe configured but plugin is not loaded");
            }
        }
        return new AuthenticationConfiguration(authenticationProvider);
    }
}
