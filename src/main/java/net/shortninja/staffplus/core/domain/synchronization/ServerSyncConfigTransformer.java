package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.IConfigTransformer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServerSyncConfigTransformer implements IConfigTransformer<ServerSyncConfig, String> {

    @ConfigProperty("server-name")
    private String serverName;

    @Override
    public ServerSyncConfig mapConfig(String s) {
        List<String> servers = new ArrayList<>(Arrays.asList(s.split("\\s*;\\s*")));
        servers.add(serverName);
        return new ServerSyncConfig(servers.stream()
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList()));
    }
}
