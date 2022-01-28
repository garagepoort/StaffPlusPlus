package net.shortninja.staffplus.core.domain.commanddetection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;

import java.util.List;

@IocBean
public class CommandDetectionConfiguration {

    @ConfigProperty("command-detection.enabled")
    public boolean enabled;

    @ConfigProperty("command-detection.command-groups")
    @ConfigTransformer(CommandDetectionGroupTransformer.class)
    public List<CommandDetectionGroupConfiguration> commandsToDetect;
}
