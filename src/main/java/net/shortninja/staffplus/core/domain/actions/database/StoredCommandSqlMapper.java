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
            10,
            8,
            12,
            2,
            1,
            6,
            7,
            9,
            11,
            4,
            3,
            13,
            mapRollback(rs),
            14);
    }

    private StoredCommandEntity mapRollback(ResultSet rs) throws SQLException {
        return getStoredCommandEntity(rs,
            24,
            22,
            26,
            16,
            15,
            20,
            21,
            23,
            25,
            18,
            17,
            27,
            null,
            28);
    }

    private StoredCommandEntity getStoredCommandEntity(ResultSet rs,
                                                       int target_run_strategy,
                                                       int target_uuid,
                                                       int execution_timestamp,
                                                       int actionable_id,
                                                       int id,
                                                       int command,
                                                       int executor_uuid,
                                                       int executor_run_strategy,
                                                       int creation_timestamp,
                                                       int rollback_timestamp,
                                                       int actionable_type,
                                                       int serverName,
                                                       StoredCommandEntity storedCommandEntity,
                                                       int is_delayed) throws SQLException {
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
            UUID.fromString(rs.getString(executor_uuid)),
            targetUuid,
            ActionRunStrategy.valueOf(rs.getString(executor_run_strategy)),
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
