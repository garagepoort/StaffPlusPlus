package net.shortninja.staffplus.core.authentication.authme;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.shortninja.staffplus.core.authentication.AuthenticationException;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.application.IocBean;
import org.bukkit.entity.Player;

@IocBean(conditionalOnProperty = "authentication.provider=authme")
public class AuthMeAuthenticationService implements AuthenticationService {

    @Override
    public void checkAuthentication(Player player) {
        if(!AuthMeApi.getInstance().isAuthenticated(player)) {
            throw new AuthenticationException();
        }
    }
}
