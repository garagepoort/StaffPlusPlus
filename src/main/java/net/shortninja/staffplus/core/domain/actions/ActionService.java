package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.actions.database.StoredCommandRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.Actionable;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class ActionService {

    private final PlayerManager playerManager;
    private final StoredCommandRepository storedCommandRepository;
    private final Options options;

    public ActionService(PlayerManager playerManager, StoredCommandRepository storedCommandRepository, Options options) {
        this.playerManager = playerManager;
        this.storedCommandRepository = storedCommandRepository;
        this.options = options;
    }

    public List<StoredCommandEntity> createCommands(List<CreateStoredCommandRequest> commands) {
        return commands.stream()
            .map(this::createCommand)
            .filter(StoredCommandEntity::isExecuted)
            .collect(Collectors.toList());
    }

    public StoredCommandEntity createCommand(CreateStoredCommandRequest request) {
        request.validate();
        StoredCommandEntity commandEntity = new StoredCommandEntity(request, isDelayed(request));
        int id = storedCommandRepository.saveCommand(commandEntity);
        commandEntity.setId(id);
        if (canExecute(commandEntity)) {
            executeCommand(commandEntity, id);
        }
        return commandEntity;
    }

    public void rollbackActionable(Actionable actionable) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(actionable.getTargetUuid());
        if (!target.isPresent()) {
            return;
        }

        storedCommandRepository.getCommandsFor(actionable)
            .stream().filter(a -> a.isExecuted() && a.isRollbackable() && !a.isRollbacked())
            .forEach(this::rollbackCommand);
    }

    public void rollbackActionable(Actionable actionable, String group) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(actionable.getTargetUuid());
        if (!target.isPresent()) {
            return;
        }

        storedCommandRepository.getCommandsFor(actionable, group)
            .stream().filter(a -> a.isExecuted() && a.isRollbackable() && !a.isRollbacked())
            .forEach(this::rollbackCommand);
    }

    public void rollbackCommand(StoredCommandEntity storedCommandEntity) {
        if (!storedCommandEntity.isRollbackable()) {
            throw new BusinessException("This command is not rollbackable");
        }
        if (storedCommandEntity.isRollbacked()) {
            throw new BusinessException("This command has already been rollbacked");
        }

        StoredCommandEntity rollbackCommand = storedCommandEntity.getRollbackCommand().get();
        boolean delayed = isDelayed(rollbackCommand);
        if (delayed) {
            storedCommandRepository.markDelayed(rollbackCommand.getId());
            return;
        }

        if (canExecute(rollbackCommand)) {
            executeCommand(rollbackCommand, rollbackCommand.getId());
        }
        markRollbacked(rollbackCommand.getId());
    }

    public void executeDelayed(StoredCommandEntity storedCommand) {
        if (canExecute(storedCommand)) {
            executeCommand(storedCommand, storedCommand.getId());
        }
    }

    private void executeCommand(StoredCommandEntity storedCommandEntity, Integer id) {
        CommandSender executioner = storedCommandEntity.getExecutionerUuid().equals(CONSOLE_UUID) ? Bukkit.getConsoleSender() : playerManager.getOnlinePlayer(storedCommandEntity.getExecutionerUuid()).get().getPlayer();
        storedCommandRepository.markExecuted(id);
        storedCommandEntity.setExecutionTimestamp(System.currentTimeMillis());
        sendEvent(new CommandExecutedEvent(executioner, storedCommandEntity.getCommand()));
    }

    private boolean isDelayed(CreateStoredCommandRequest storedCommandEntity) {
        boolean executionerDelayed = !storedCommandEntity.getExecutioner().equals(CONSOLE_UUID) && storedCommandEntity.getExecutionerRunStrategy() == ActionRunStrategy.DELAY && !playerManager.getOnlinePlayer((storedCommandEntity.getExecutioner())).isPresent();
        boolean targetDelayed = storedCommandEntity.getTarget().isPresent() && storedCommandEntity.getTargetRunStrategy().orElse(null) == ActionRunStrategy.DELAY && !playerManager.getOnlinePlayer(storedCommandEntity.getTarget().get().getUniqueId()).isPresent();
        return executionerDelayed || targetDelayed;
    }

    private boolean isDelayed(StoredCommandEntity storedCommandEntity) {
        boolean executionerDelayed = !storedCommandEntity.getExecutionerUuid().equals(CONSOLE_UUID) && storedCommandEntity.getExecutionerRunStrategy() == ActionRunStrategy.DELAY && !playerManager.getOnlinePlayer((storedCommandEntity.getExecutionerUuid())).isPresent();
        boolean targetDelayed = storedCommandEntity.getTargetUuid().isPresent() && storedCommandEntity.getTargetRunStrategy().orElse(null) == ActionRunStrategy.DELAY && !playerManager.getOnlinePlayer(storedCommandEntity.getTargetUuid().get()).isPresent();
        return executionerDelayed || targetDelayed;
    }

    private boolean canExecute(StoredCommandEntity storedCommandEntity) {
        if (storedCommandEntity.getServerName().isPresent() && !storedCommandEntity.getServerName().get().equals(options.serverName)) {
            return false;
        }
        boolean executionerOnline = storedCommandEntity.getExecutionerUuid().equals(CONSOLE_UUID) || playerManager.getOnlinePlayer(storedCommandEntity.getExecutionerUuid()).isPresent();
        boolean targetNotPresentOrOnline = !storedCommandEntity.getTargetUuid().isPresent() || playerManager.getOnlinePlayer(storedCommandEntity.getTargetUuid().get()).isPresent();
        return executionerOnline && (targetNotPresentOrOnline || storedCommandEntity.getTargetRunStrategy().get() == ActionRunStrategy.ALWAYS);
    }

    public void markRollbacked(int executableActionId) {
        storedCommandRepository.markRollbacked(executableActionId);
    }

    public Collection<StoredCommandEntity> getActions(Actionable actionable) {
        return storedCommandRepository.getCommandsFor(actionable);
    }
}
