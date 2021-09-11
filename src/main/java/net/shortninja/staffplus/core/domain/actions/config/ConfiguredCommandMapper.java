package net.shortninja.staffplus.core.domain.actions.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest.CreateStoredCommandRequestBuilder.commandBuilder;

@IocBean
public class ConfiguredCommandMapper {

    private final Options options;

    public ConfiguredCommandMapper(Options options) {
        this.options = options;
    }

    public List<CreateStoredCommandRequest> toCreateRequests(Map<String, String> placeholders, Map<String, OfflinePlayer> targets, List<ConfiguredCommand> configuredCommands, List<ActionFilter> actionFilters) {
        List<CreateStoredCommandRequest> list = new ArrayList<>();
        for (ConfiguredCommand c : configuredCommands) {
            CreateStoredCommandRequest createStoredCommandRequest = toCreateRequest(placeholders, targets, c);
            if (actionFilters.stream().allMatch(a -> a.isValidAction(createStoredCommandRequest, c.getFilters()))) {
                list.add(createStoredCommandRequest);
            }
        }
        return list;
    }

    public CreateStoredCommandRequest toCreateRequest(Map<String, String> placeholders, Map<String, OfflinePlayer> targets, ConfiguredCommand c) {
        return commandBuilder()
            .command(JavaUtils.replacePlaceholders(c.getCommand(), placeholders))
            .executioner(getExecutioner(c, targets))
            .executionerRunStrategy(c.getExecutionerRunStrategy())
            .target(getTarget(c, targets))
            .targetRunStrategy(c.getTargetRunStrategy().orElse(null))
            .serverName(options.serverName)
            .build();
    }

    private UUID getExecutioner(ConfiguredCommand configuredCommand, Map<String, OfflinePlayer> targets) {
        String key = configuredCommand.getExecutioner();
        if (key.equalsIgnoreCase("console")) {
            return Constants.CONSOLE_UUID;
        }
        if (!targets.containsKey(key)) {
            throw new ConfigurationException("No executioner [" + key + "] know for this command configuration");
        }
        return targets.get(key).getUniqueId();
    }

    private OfflinePlayer getTarget(ConfiguredCommand configuredCommand, Map<String, OfflinePlayer> targets) {
        if (!configuredCommand.getTarget().isPresent()) {
            return null;
        }
        String key = configuredCommand.getTarget().get();
        if (!targets.containsKey(key)) {
            throw new ConfigurationException("No target [" + key + "] know for this command configuration");
        }
        return targets.get(key);
    }
}
