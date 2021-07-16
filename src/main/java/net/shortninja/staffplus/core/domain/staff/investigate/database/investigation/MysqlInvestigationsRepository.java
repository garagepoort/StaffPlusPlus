package net.shortninja.staffplus.core.domain.staff.investigate.database.investigation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlInvestigationsRepository extends AbstractSqlInvestigationsRepository {

    public MysqlInvestigationsRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        super(playerManager, sqlConnectionProvider, options);
    }

    @Override
    public int addInvestigation(Investigation investigation) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_investigations(investigator_uuid, investigated_uuid, status, creation_timestamp, server_name) " +
                 "VALUES(?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, investigation.getInvestigatorUuid().toString());
            if(investigation.getInvestigatedUuid().isPresent()) {
                insert.setString(2, investigation.getInvestigatedUuid().get().toString());
            }else {
                insert.setNull(2, Types.VARCHAR);
            }
            insert.setString(3, investigation.getStatus().name());
            insert.setLong(4, investigation.getCreationTimestamp());
            insert.setString(5, options.serverName);
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
