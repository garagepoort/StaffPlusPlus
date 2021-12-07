package net.shortninja.staffplus.core.domain.player.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.database.SqlRepository;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class PlayerRepository extends SqlRepository {

    public PlayerRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }

    public Optional<StoredPlayer> findPlayer(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_players WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildStoredPlayer(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    public int save(StoredPlayer player) {
        try (Connection sql = getConnection()) {
            sql.setAutoCommit(false);
            PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_players(uuid, name, servers) " +
                " VALUES(? ,?, ?);", Statement.RETURN_GENERATED_KEYS);
            sql.setAutoCommit(false);
            insert.setString(1, player.getUuid().toString());
            insert.setString(2, player.getName());
            insert.setString(3, String.join(";", player.getServers()));
            insert.executeUpdate();
            int id = getGeneratedId(sql, insert);
            sql.setAutoCommit(true);
            return id;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void update(StoredPlayer player) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_players set name=?, servers=? WHERE ID=?")) {
            insert.setString(1, player.getName());
            insert.setString(2, String.join(";", player.getServers()));
            insert.setInt(3, player.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private StoredPlayer buildStoredPlayer(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        UUID playerUuid = UUID.fromString(rs.getString("uuid"));
        String name = rs.getString("name");
        Set<String> servers = rs.getString("servers") == null ? new HashSet<>() : Arrays.stream(rs.getString("servers").split(";")).collect(Collectors.toSet());
        return new StoredPlayer(
            id,
            playerUuid,
            name,
            servers);
    }
}
