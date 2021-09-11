package net.shortninja.staffplus.core.domain.actions.database;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;
import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@IocBean
public class StoredCommandSqlMapper {

    public StoredCommandEntity map(ResultSet rs) throws SQLException {
        return getStoredCommandEntity(rs,
            "sp_commands.target_run_strategy",
            "sp_commands.target_uuid",
            "sp_commands.execution_timestamp",
            "sp_commands.actionable_id",
            "sp_commands.id",
            "sp_commands.command",
            "sp_commands.executioner_uuid",
            "sp_commands.executioner_run_strategy",
            "sp_commands.creation_timestamp",
            "sp_commands.rollback_timestamp",
            "sp_commands.actionable_type",
            "sp_commands.server_name",
            mapRollback(rs), "sp_commands.is_delayed");
    }

    private StoredCommandEntity mapRollback(ResultSet rs) throws SQLException {
        return getStoredCommandEntity(rs,
            "rollbackcommand.target_run_strategy",
            "rollbackcommand.target_uuid",
            "rollbackcommand.execution_timestamp",
            "rollbackcommand.actionable_id",
            "rollbackcommand.id",
            "rollbackcommand.command",
            "rollbackcommand.executioner_uuid",
            "rollbackcommand.executioner_run_strategy",
            "rollbackcommand.creation_timestamp",
            "rollbackcommand.rollback_timestamp",
            "rollbackcommand.actionable_type",
            "rollbackcommand.server_name",
            null,
            "rollbackcommand.is_delayed");
    }

    private StoredCommandEntity getStoredCommandEntity(ResultSet rs,
                                                       String target_run_strategy,
                                                       String target_uuid,
                                                       String execution_timestamp,
                                                       String actionable_id,
                                                       String id,
                                                       String command,
                                                       String executioner_uuid,
                                                       String executioner_run_strategy,
                                                       String creation_timestamp,
                                                       String rollback_timestamp,
                                                       String actionable_type,
                                                       String serverName,
                                                       StoredCommandEntity storedCommandEntity,
                                                       String is_delayed) throws SQLException {
        Integer commandId = rs.getInt(id);
        commandId = rs.wasNull() ? null : commandId;
        if(commandId == null) {
            return null;
        }

        ActionRunStrategy targetRunStrategy = rs.getString(target_run_strategy) != null ? ActionRunStrategy.valueOf(rs.getString(target_run_strategy)) : null;
        UUID targetUuid = rs.getString(target_uuid) != null ? UUID.fromString(rs.getString(target_uuid)) : null;

        Long executionTimestamp = rs.getLong(execution_timestamp);
        executionTimestamp = rs.wasNull() ? null : executionTimestamp;

        Long rollbackTimestamp = rs.getLong(rollback_timestamp);
        rollbackTimestamp = rs.wasNull() ? null : rollbackTimestamp;

        Integer actionableId = rs.getInt(actionable_id);
        actionableId = rs.wasNull() ? null : actionableId;

        return new StoredCommandEntity(
            commandId,
            rs.getString(command),
            UUID.fromString(rs.getString(executioner_uuid)),
            targetUuid,
            ActionRunStrategy.valueOf(rs.getString(executioner_run_strategy)),
            targetRunStrategy,
            rs.getLong(creation_timestamp),
            executionTimestamp,
            rollbackTimestamp,
            actionableId,
            rs.getString(actionable_type),
            rs.getString(serverName),
            storedCommandEntity,
            rs.getBoolean(is_delayed));
    }

}
