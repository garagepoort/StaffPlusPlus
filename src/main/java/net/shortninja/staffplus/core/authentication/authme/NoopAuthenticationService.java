package net.shortninja.staffplus.core.authentication.authme;

import net.shortninja.staffplus.core.authentication.AuthenticationService;
import be.garagepoort.mcioc.IocBean;
import org.bukkit.entity.Player;


@IocBean(conditionalOnProperty = "authentication.provider=noop")
public class NoopAuthenticationService implements AuthenticationService {

    @Override
    public void checkAuthentication(Player player) {
        // No authentication supported
    }
}
