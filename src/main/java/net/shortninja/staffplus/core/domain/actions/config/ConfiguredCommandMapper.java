package net.shortninja.staffplus.core.domain.actions.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplus.core.domain.actions.PermissionActionFilter;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest.CreateStoredCommandRequestBuilder.commandBuilder;

@IocBean
public class ConfiguredCommandMapper {

    private final Options options;
    private final PermissionActionFilter permissionActionFilter;

    public ConfiguredCommandMapper(Options options, PermissionActionFilter permissionActionFilter) {
        this.options = options;
        this.permissionActionFilter = permissionActionFilter;
    }

    public List<CreateStoredCommandRequest> toCreateRequests(List<ConfiguredCommand> configuredCommands, Map<String, String> placeholders, Map<String, OfflinePlayer> targets, List<ActionFilter> actionFilters) {
        List<ActionFilter> filters = new ArrayList<>(actionFilters);
        filters.add(permissionActionFilter);

        List<CreateStoredCommandRequest> list = new ArrayList<>();
        for (ConfiguredCommand c : configuredCommands) {
            CreateStoredCommandRequest createStoredCommandRequest = toCreateRequest(c, placeholders, targets);
            if (filters.stream().allMatch(a -> a.isValidAction(createStoredCommandRequest, c.getFilters()))) {
                list.add(createStoredCommandRequest);
            }
        }
        return list;
    }

    public CreateStoredCommandRequest toCreateRequest(ConfiguredCommand c, Map<String, String> placeholders, Map<String, OfflinePlayer> targets) {

        OfflinePlayer target = getTarget(c, targets);
        UUID executionerUuid;
        if (c.getExecutioner().equalsIgnoreCase("console")) {
            executionerUuid = CONSOLE_UUID;
            placeholders.put("%executioner%", "console");
        } else {
            OfflinePlayer executionerPlayer = getExecutioner(c, targets);
            executionerUuid = executionerPlayer.getUniqueId();
            placeholders.put("%executioner%", executionerPlayer.getName());
        }

        if (target != null) {
            placeholders.put("%target%", target.getName());
        }
        return commandBuilder()
            .command(JavaUtils.replacePlaceholders(c.getCommand(), placeholders))
            .executioner(executionerUuid)
            .executionerRunStrategy(c.getExecutionerRunStrategy())
            .target(target)
            .targetRunStrategy(c.getTargetRunStrategy().orElse(null))
            .serverName(options.serverName)
            .build();
    }

    private OfflinePlayer getExecutioner(ConfiguredCommand configuredCommand, Map<String, OfflinePlayer> targets) {
        String key = configuredCommand.getExecutioner();
        if (!targets.containsKey(key)) {
            throw new ConfigurationException("No executioner [" + key + "] know for this command configuration");
        }
        return targets.get(key);
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
