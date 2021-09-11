package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

public class CreateStoredCommandRequest {

    private final UUID executioner;
    private final ActionRunStrategy executionerRunStrategy;
    private final OfflinePlayer target;
    private final ActionRunStrategy targetRunStrategy;
    private final String command;
    private final CreateStoredCommandRequest rollbackCommand;
    private final Integer actionableId;
    private final String actionableType;
    private final String serverName;

    public CreateStoredCommandRequest(UUID executioner,
                                      ActionRunStrategy executionerRunStrategy,
                                      OfflinePlayer target,
                                      ActionRunStrategy targetRunStrategy,
                                      String command,
                                      CreateStoredCommandRequest rollbackCommand,
                                      Integer actionableId,
                                      String actionableType,
                                      String serverName) {
        this.executioner = executioner;
        this.executionerRunStrategy = executionerRunStrategy;
        this.target = target;
        this.targetRunStrategy = targetRunStrategy;
        this.command = command;
        this.rollbackCommand = rollbackCommand;
        this.actionableId = actionableId;
        this.actionableType = actionableType;
        this.serverName = serverName;
    }

    public UUID getExecutioner() {
        return executioner;
    }

    public ActionRunStrategy getExecutionerRunStrategy() {
        return executionerRunStrategy;
    }

    public Optional<OfflinePlayer> getTarget() {
        return Optional.ofNullable(target);
    }

    public Optional<ActionRunStrategy> getTargetRunStrategy() {
        return Optional.ofNullable(targetRunStrategy);
    }

    public String getCommand() {
        return command;
    }

    public Optional<CreateStoredCommandRequest> getRollbackCommand() {
        return Optional.ofNullable(rollbackCommand);
    }

    public Optional<Integer> getActionableId() {
        return Optional.ofNullable(actionableId);
    }

    public Optional<String> getActionableType() {
        return Optional.ofNullable(actionableType);
    }

    public String getServerName() {
        return serverName;
    }

    public void validate() {
        if (executioner == null) {
            throw new BusinessException("Executioner is mandatory");
        }
        if (executionerRunStrategy == null) {
            throw new BusinessException("executionerRunStrategy is mandatory");
        }
        if (target != null && targetRunStrategy == null) {
            throw new BusinessException("targetRunStrategy is mandatory when target filled in");
        }
        if (StringUtils.isBlank(command)) {
            throw new BusinessException("command is mandatory");
        }
        if (StringUtils.isBlank(serverName)) {
            throw new BusinessException("serverName is mandatory");
        }
        if (rollbackCommand != null) {
            rollbackCommand.validate();
        }
    }

    public static class CreateStoredCommandRequestBuilder {
        private UUID executioner;
        private ActionRunStrategy executionerRunStrategy;
        private OfflinePlayer target;
        private ActionRunStrategy targetRunStrategy;
        private String command;
        private CreateStoredCommandRequest rollbackCommand;
        private Integer actionableId;
        private String actionableType;
        private String serverName;

        public static CreateStoredCommandRequestBuilder commandBuilder() {
            return new CreateStoredCommandRequestBuilder();
        }

        public CreateStoredCommandRequestBuilder executioner(UUID executioner) {
            this.executioner = executioner;
            return this;
        }

        public CreateStoredCommandRequestBuilder executionerRunStrategy(ActionRunStrategy executionerRunStrategy) {
            this.executionerRunStrategy = executionerRunStrategy;
            return this;
        }

        public CreateStoredCommandRequestBuilder target(OfflinePlayer target) {
            this.target = target;
            return this;
        }

        public CreateStoredCommandRequestBuilder targetRunStrategy(ActionRunStrategy targetRunStrategy) {
            this.targetRunStrategy = targetRunStrategy;
            return this;
        }

        public CreateStoredCommandRequestBuilder command(String command) {
            this.command = command;
            return this;
        }

        public CreateStoredCommandRequestBuilder rollbackCommand(CreateStoredCommandRequest rollbackCommand) {
            this.rollbackCommand = rollbackCommand;
            return this;
        }

        public CreateStoredCommandRequestBuilder actionableId(Integer actionableId) {
            this.actionableId = actionableId;
            return this;
        }

        public CreateStoredCommandRequestBuilder actionableType(String actionableType) {
            this.actionableType = actionableType;
            return this;
        }

        public CreateStoredCommandRequestBuilder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public CreateStoredCommandRequest build() {
            return new CreateStoredCommandRequest(executioner, executionerRunStrategy, target, targetRunStrategy, command, rollbackCommand, actionableId, actionableType, serverName);
        }
    }
}
