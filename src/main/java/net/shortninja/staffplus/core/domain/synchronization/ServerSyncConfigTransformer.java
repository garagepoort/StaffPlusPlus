package net.shortninja.staffplus.core.domain.synchronization;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;

import java.util.Arrays;
import java.util.List;

public class ServerSyncConfigTransformer implements IConfigTransformer<ServerSyncConfig, String> {

    @Override
    public ServerSyncConfig mapConfig(String s) {
        List<String> servers = Arrays.asList(s.split("\\s*;\\s*"));
        servers.add(StaffPlus.get().getIocContainer().get(Options.class).serverName);
        return new ServerSyncConfig(servers);
    }
}
