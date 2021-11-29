package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;

@IocBean
public class ServerSyncConfiguration {

    @ConfigProperty("server-sync-module.vanish-sync")
    public boolean vanishSyncEnabled;

    @ConfigProperty("server-sync-module.staffmode-sync")
    public boolean staffModeSyncEnabled;

    @ConfigProperty("server-sync-module.ban-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig banSyncServers;

    @ConfigProperty("server-sync-module.report-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig reportSyncServers;

    @ConfigProperty("server-sync-module.warning-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig warningSyncServers;

    @ConfigProperty("server-sync-module.mute-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig muteSyncServers;

    @ConfigProperty("server-sync-module.kick-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig kickSyncServers;

    @ConfigProperty("server-sync-module.investigation-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig investigationSyncServers;

    @ConfigProperty("server-sync-module.notes-sync")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    public ServerSyncConfig notesSyncServers;

    public boolean sessionSyncEnabled() {
        return vanishSyncEnabled || staffModeSyncEnabled;
    }

}
