package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.configuration.IConfigTransformer;

import java.util.Arrays;

public class ServerSyncConfigTransformer implements IConfigTransformer<ServerSyncConfig, String> {

    @Override
    public ServerSyncConfig mapConfig(String s) {
        return new ServerSyncConfig(Arrays.asList(s.split("\\s*;\\s*")));
    }
}
