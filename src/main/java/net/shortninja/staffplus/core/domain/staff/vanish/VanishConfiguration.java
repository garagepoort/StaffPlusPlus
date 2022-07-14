package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class VanishConfiguration {

    @ConfigProperty("vanish-module.enabled")
    public boolean enabled;
    @ConfigProperty("vanish-module.tab-list")
    public boolean vanishTabList;
    @ConfigProperty("vanish-module.show-away")
    public boolean vanishShowAway;
    @ConfigProperty("vanish-module.chat")
    public boolean vanishChatEnabled;
    @ConfigProperty("vanish-module.vanish-message-enabled")
    public boolean vanishMessageEnabled;
    @ConfigProperty("vanish-module.night-vision")
    public boolean nightVisionEnabled;

    @ConfigProperty("permissions:vanish")
    public String permissionVanishCommand;
    @ConfigProperty("permissions:see-vanished")
    public String permissionSeeVanished;
    @ConfigProperty("permissions:vanish-total")
    public String permissionVanishTotal;
    @ConfigProperty("permissions:vanish-list")
    public String permissionVanishList;
    @ConfigProperty("permissions:vanish-player")
    public String permissionVanishPlayer;

}
