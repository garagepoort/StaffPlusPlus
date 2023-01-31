package net.shortninja.staffplus.core.protection.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class ProtectConfiguration {

    @ConfigProperty("protect-module.player-enabled")
    public boolean playerProtectEnabled;
    @ConfigProperty("protect-module.area-enabled")
    public boolean areaProtectEnabled;

    @ConfigProperty("protect-module.area-max-size")
    public int areaMaxSize;
    @ConfigProperty("staffmode-modules:modules.gui-module.protected-areas-gui")
    public boolean modeGuiProtectedAreas;
    @ConfigProperty("staffmode-modules:modules.gui-module.protected-areas-title")
    public String modeGuiProtectedAreasTitle;
    @ConfigProperty("permissions:protect-player")
    public String permissionProtectPlayer;
    @ConfigProperty("permissions:protect-area")
    public String permissionProtectArea;
}
