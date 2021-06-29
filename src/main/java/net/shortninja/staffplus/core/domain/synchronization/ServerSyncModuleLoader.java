package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;

@IocBean
public class ServerSyncModuleLoader extends AbstractConfigLoader<ServerSyncConfiguration> {
    public static final String SERVER_SYNC_MODULE = "server-sync-module.";

    @Override
    protected ServerSyncConfiguration load() {
        return new ServerSyncConfiguration(
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "vanish-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "staffmode-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "ban-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "report-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "warning-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "mute-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "kick-sync"),
            defaultConfig.getBoolean(SERVER_SYNC_MODULE + "investigation-sync")
        );
    }
}
