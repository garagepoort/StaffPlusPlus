package net.shortninja.staffplus.authentication.authme;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.shortninja.staffplus.authentication.AuthenticationException;
import net.shortninja.staffplus.authentication.AuthenticationService;
import org.bukkit.entity.Player;

public class AuthMeAuthenticationService implements AuthenticationService {

    @Override
    public void checkAuthentication(Player player) {
        if(!AuthMeApi.getInstance().isAuthenticated(player)) {
            throw new AuthenticationException();
        }
    }
}
