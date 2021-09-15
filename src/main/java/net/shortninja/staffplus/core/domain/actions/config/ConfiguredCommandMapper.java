package net.shortninja.staffplus.core.domain.actions.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest.CreateStoredCommandRequestBuilder;
import net.shortninja.staffplus.core.domain.actions.PermissionActionFilter;
import net.shortninja.staffplusplus.Actionable;
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
        return toCreateRequests(null, configuredCommands, placeholders, targets, actionFilters);
    }

    public List<CreateStoredCommandRequest> toCreateRequests(Actionable actionable, List<ConfiguredCommand> configuredCommands, Map<String, String> placeholders, Map<String, OfflinePlayer> targets, List<ActionFilter> actionFilters) {
        List<ActionFilter> filters = new ArrayList<>(actionFilters);
        filters.add(permissionActionFilter);

        List<CreateStoredCommandRequest> list = new ArrayList<>();
        for (ConfiguredCommand c : configuredCommands) {
            CreateStoredCommandRequest createStoredCommandRequest = toCreateRequest(actionable, c, placeholders, targets);
            if (filters.stream().allMatch(a -> a.isValidAction(createStoredCommandRequest, c.getFilters()))) {
                list.add(createStoredCommandRequest);
            }
        }
        return list;
    }

    public CreateStoredCommandRequest toCreateRequest(ConfiguredCommand c, Map<String, String> placeholders, Map<String, OfflinePlayer> targets) {
        return toCreateRequest(null, c, placeholders, targets);
    }

    public CreateStoredCommandRequest toCreateRequest(Actionable actionable, ConfiguredCommand c, Map<String, String> placeholders, Map<String, OfflinePlayer> targets) {

        OfflinePlayer target = getTarget(c, targets);
        UUID executorUuid;
        if (c.getExecutor().equalsIgnoreCase("console")) {
            executorUuid = CONSOLE_UUID;
            placeholders.put("%executor%", "console");
        } else {
            OfflinePlayer executorPlayer = getExecutor(c, targets);
            executorUuid = executorPlayer.getUniqueId();
            placeholders.put("%executor%", executorPlayer.getName());
        }

        if (target != null) {
            placeholders.put("%target%", target.getName());
        }

        CreateStoredCommandRequestBuilder builder = commandBuilder()
            .command(JavaUtils.replacePlaceholders(c.getCommand(), placeholders))
            .executor(executorUuid)
            .executorRunStrategy(c.getExecutorRunStrategy())
            .target(target)
            .targetRunStrategy(c.getTargetRunStrategy().orElse(null))
            .serverName(options.serverName);

        if (actionable != null) {
            builder
                .actionableId(actionable.getId())
                .actionableType(actionable.getActionableType());
        }

        if (c.getRollbackCommand().isPresent()) {
            builder.rollbackCommand(toCreateRequest(actionable, c.getRollbackCommand().get(), placeholders, targets));
        }

        return builder.build();
    }

    private OfflinePlayer getExecutor(ConfiguredCommand configuredCommand, Map<String, OfflinePlayer> targets) {
        String key = configuredCommand.getExecutor();
        if (!targets.containsKey(key)) {
            throw new ConfigurationException("No executor [" + key + "] know for this command configuration");
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
