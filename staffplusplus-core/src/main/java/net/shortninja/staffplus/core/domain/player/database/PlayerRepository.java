package net.shortninja.staffplus.core.domain.player.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class PlayerRepository {

    private final QueryBuilderFactory query;

    public PlayerRepository(QueryBuilderFactory query) {
        this.query = query;
    }

    public Optional<StoredPlayer> findPlayer(UUID uuid) {
        return query.create().findOne("SELECT * FROM sp_players WHERE uuid = ?", ps -> ps.setString(1, uuid.toString()), this::buildStoredPlayer);
    }

    public int save(StoredPlayer player) {
        return query.create().insertQuery("INSERT INTO sp_players(uuid, name, servers) " +
            " VALUES(? ,?, ?);", insert -> {
            insert.setString(1, player.getUuid().toString());
            insert.setString(2, player.getName());
            insert.setString(3, String.join(";", player.getServers()));
        });
    }

    public void update(StoredPlayer player) {
        query.create().updateQuery("UPDATE sp_players set name=?, servers=? WHERE ID=?", insert -> {
            insert.setString(1, player.getName());
            insert.setString(2, String.join(";", player.getServers()));
            insert.setInt(3, player.getId());
        });
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
