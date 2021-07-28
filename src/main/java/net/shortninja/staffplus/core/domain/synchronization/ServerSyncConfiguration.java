package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class ServerSyncConfiguration {

    @ConfigProperty("server-sync-module.vanish-sync")
    public boolean vanishSyncEnabled;
    @ConfigProperty("server-sync-module.staffmode-sync")
    public boolean staffModeSyncEnabled;
    @ConfigProperty("server-sync-module.ban-sync")
    public boolean banSyncEnabled;
    @ConfigProperty("server-sync-module.report-sync")
    public boolean reportSyncEnabled;
    @ConfigProperty("server-sync-module.warning-sync")
    public boolean warningSyncEnabled;
    @ConfigProperty("server-sync-module.mute-sync")
    public boolean muteSyncEnabled;
    @ConfigProperty("server-sync-module.kick-sync")
    public boolean kickSyncEnabled;
    @ConfigProperty("server-sync-module.investigation-sync")
    public boolean investigationSyncEnabled;

    public boolean sessionSyncEnabled() {
        return vanishSyncEnabled || staffModeSyncEnabled;
    }

}
