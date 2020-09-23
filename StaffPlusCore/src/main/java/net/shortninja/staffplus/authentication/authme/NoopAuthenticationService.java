package net.shortninja.staffplus.authentication.authme;

import net.shortninja.staffplus.authentication.AuthenticationService;
import org.bukkit.entity.Player;

public class NoopAuthenticationService implements AuthenticationService {

    @Override
    public void checkAuthentication(Player player) {
        // No authentication supported
    }
}
