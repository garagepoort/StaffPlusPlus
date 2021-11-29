package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.IConfigTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerSyncConfigTransformer implements IConfigTransformer<ServerSyncConfig, String> {

    @ConfigProperty("server-name")
    private String serverName;

    @Override
    public ServerSyncConfig mapConfig(String s) {
        List<String> servers = new ArrayList<>(Arrays.asList(s.split("\\s*;\\s*")));
        servers.add(serverName);
        return new ServerSyncConfig(servers);
    }
}
