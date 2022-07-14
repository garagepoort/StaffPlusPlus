package net.shortninja.staffplus.core.domain.player.ip.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilder;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@IocBean
public class PlayerIpRepositoryImpl implements PlayerIpRepository {
    private final QueryBuilderFactory query;

    public PlayerIpRepositoryImpl(QueryBuilderFactory query) {
        this.query = query;
    }

    @Override
    public void save(UUID playerUuid, String playerName, String ip, String server) {
        QueryBuilder query = this.query.create().startTransaction();
        query.deleteQuery("DELETE FROM sp_player_ips WHERE player_uuid = ? AND ip=?;",
            delete -> {
                delete.setString(1, playerUuid.toString());
                delete.setString(2, ip);
            });
        query.insertQuery("INSERT INTO sp_player_ips(player_uuid, player_name, ip, ip_numeric, server_name, timestamp) VALUES (?,?,?,?,?,?);",
            insert -> {
                insert.setString(1, playerUuid.toString());
                insert.setString(2, playerName);
                insert.setString(3, ip);
                insert.setLong(4, JavaUtils.convertIp(ip));
                insert.setString(5, server);
                insert.setLong(6, System.currentTimeMillis());
            });
        query.commit();
    }

    @Override
    public List<String> getIps(UUID playerUuid) {
        return query.create().find("SELECT ip FROM sp_player_ips WHERE player_uuid = ?",
            (ps) -> ps.setString(1, playerUuid.toString()),
            (rs) -> rs.getString(1));
    }

    @Override
    public List<PlayerIpRecord> getAllIpRecords(Connection connection) {
        return query.create(connection).find("SELECT ip, player_uuid, player_name FROM sp_player_ips;", this::buildPlayerIpRecord);
    }

    @Override
    public Optional<String> getLastIp(UUID playerUuid) {
        return query.create().findOne("SELECT ip FROM sp_player_ips WHERE player_uuid = ? ORDER BY timestamp DESC LIMIT 1",
            (ps) -> ps.setString(1, playerUuid.toString()),
            (rs) -> rs.getString(1));
    }

    @Override
    public List<PlayerIpRecord> findInSubnet(long lower, long upper) {
        return query.create().find("SELECT ip, player_uuid, player_name FROM sp_player_ips s1 WHERE ip_numeric BETWEEN ? AND ? AND timestamp = (SELECT MAX(timestamp) FROM sp_player_ips s2 WHERE s1.player_uuid = s2.player_uuid);",
            (ps) -> {
                ps.setLong(1, lower);
                ps.setLong(2, upper);
            },
            this::buildPlayerIpRecord);
    }

    @Override
    public List<PlayerIpRecord> findWithIp(long ip) {
        return query.create().find("SELECT ip, player_uuid, player_name FROM sp_player_ips s1 WHERE ip_numeric = ? AND timestamp = (SELECT MAX(timestamp) FROM sp_player_ips s2 WHERE s1.player_uuid = s2.player_uuid);",
            (ps) -> ps.setLong(1, ip),
            this::buildPlayerIpRecord);
    }

    @NotNull
    private PlayerIpRecord buildPlayerIpRecord(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString(2));
        return new PlayerIpRecord(rs.getString(1), playerUuid, rs.getString(3));
    }

    @Override
    public void deleteRecordsFor(SppPlayer player) {
        query.create().deleteQuery("DELETE FROM sp_player_ips WHERE player_uuid = ?;", (delete) -> delete.setString(1, player.getId().toString()));
    }
}
