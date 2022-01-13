package net.shortninja.staffplus.core.domain.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.database.SqlRepository;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@IocBean
public class ChatChannelRepository extends SqlRepository {

    public ChatChannelRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }

    public Optional<ChatChannel> findChatChannel(String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_chat_channels WHERE channel_id = ? AND type = ? " + Constants.getServerNameFilterWithAnd(serverSyncConfig))) {
            ps.setString(1, channelId);
            ps.setString(2, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildChatChannel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    public List<UUID> findChatChannelMembers(int channelId) {
        List<UUID> members = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_chat_channel_members WHERE channel_id = ?")) {
            ps.setInt(1, channelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(UUID.fromString(rs.getString("player_uuid")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return members;
    }

    public int save(ChatChannel channel) {
        try (Connection sql = getConnection()) {
            sql.setAutoCommit(false);
            PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_chat_channels(channel_id, type, server_name) " +
                " VALUES(? ,?, ?);", Statement.RETURN_GENERATED_KEYS);
            sql.setAutoCommit(false);
            insert.setString(1, channel.getChannelId());
            insert.setString(2, channel.getType().name());
            insert.setString(3, channel.getServerName());
            insert.executeUpdate();
            int id = getGeneratedId(sql, insert);

            for (UUID member : channel.getMembers()) {
                PreparedStatement insertMembers = sql.prepareStatement("INSERT INTO sp_chat_channel_members(channel_id, player_uuid) VALUES(? ,?);");
                sql.setAutoCommit(false);
                insertMembers.setInt(1, id);
                insertMembers.setString(2, member.toString());
                insertMembers.executeUpdate();
            }

            sql.setAutoCommit(true);
            return id;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private ChatChannel buildChatChannel(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String channelId = rs.getString("channel_id");
        String serverName = rs.getString("server_name");
        ChatChannelType type = ChatChannelType.valueOf(rs.getString("type"));
        List<UUID> members = findChatChannelMembers(id);
        return new ChatChannel(
            id,
            channelId,
            members,
            type,
            serverName);
    }

    public void delete(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_chat_channels WHERE id = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_chat_channel_members WHERE channel_id = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
