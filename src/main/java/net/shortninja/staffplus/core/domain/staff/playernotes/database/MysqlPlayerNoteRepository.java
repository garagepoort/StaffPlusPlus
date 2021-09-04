package net.shortninja.staffplus.core.domain.staff.playernotes.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlPlayerNoteRepository extends AbstractPlayerNoteRepository {

    public MysqlPlayerNoteRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(options, sqlConnectionProvider);
    }

    @Override
    public long createPlayerNote(PlayerNote playerNote) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_player_notes (note, target_name, target_uuid, noted_by_name, noted_by_uuid, is_private_note, creation_timestamp, server_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, playerNote.getNote());
            insert.setString(2, playerNote.getTargetName());
            insert.setString(3, playerNote.getTargetUuid().toString());
            insert.setString(4, playerNote.getNotedByName());
            insert.setString(5, playerNote.getNotedByUuid().toString());
            insert.setBoolean(6, playerNote.isPrivateNote());
            insert.setLong(7, playerNote.getCreationTimestamp());
            insert.setString(8, options.serverName);
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
