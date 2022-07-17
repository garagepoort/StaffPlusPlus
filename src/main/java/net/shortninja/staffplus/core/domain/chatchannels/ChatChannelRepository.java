package net.shortninja.staffplus.core.domain.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilder;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@IocBean
public class ChatChannelRepository {
    private final QueryBuilderFactory query;

    public ChatChannelRepository(QueryBuilderFactory query) {
        this.query = query;
    }

    public Optional<ChatChannel> findChatChannel(String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        Optional<ChatChannel> chatChannel = query.create().findOne("SELECT * FROM sp_chat_channels WHERE channel_id = ? AND type = ? " + Constants.getServerNameFilterWithAnd(serverSyncConfig),
            (ps) -> {
                ps.setString(1, channelId);
                ps.setString(2, type.name());
            }, this::buildChatChannel);

        chatChannel.ifPresent(c -> c.setMembers(findChatChannelMembers(c.getId())));
        return chatChannel;
    }

    public Set<UUID> findChatChannelMembers(int channelId) {
        List<UUID> playerUuids = query.create().find("SELECT * FROM sp_chat_channel_members WHERE channel_id = ?",
            ps -> ps.setInt(1, channelId),
            rs -> UUID.fromString(rs.getString("player_uuid")));
        return new HashSet<>(playerUuids);
    }

    public int save(ChatChannel channel) {
        QueryBuilder query = this.query.create();
        int chatChannelId = query.startTransaction().insertQuery("INSERT INTO sp_chat_channels(chat_prefix, chat_line, channel_name, channel_id, type, server_name) " +
            " VALUES(?, ?, ?, ? ,?, ?);", insert -> {
            insert.setString(1, channel.getPrefix());
            insert.setString(2, channel.getLine());
            insert.setString(3, channel.getName());
            insert.setString(4, channel.getChannelId());
            insert.setString(5, channel.getType().name());
            insert.setString(6, channel.getServerName());
        });
        channel.getMembers()
            .forEach(member -> query.insertQuery("INSERT INTO sp_chat_channel_members(channel_id, player_uuid) VALUES(? ,?);", insert -> {
                insert.setInt(1, chatChannelId);
                insert.setString(2, member.toString());
            }));
        query.commit();
        return chatChannelId;
    }

    public void delete(int id) {
        QueryBuilder query = this.query.create().startTransaction();
        query.deleteQuery("DELETE FROM sp_chat_channels WHERE id = ?", (insert) -> insert.setInt(1, id));
        query.deleteQuery("DELETE FROM sp_chat_channel_members WHERE channel_id = ?", (insert) -> insert.setInt(1, id));
        query.commit();
    }

    public void addMember(ChatChannel chatChannel, SppPlayer player) {
        query.create().insertQuery("INSERT INTO sp_chat_channel_members(channel_id, player_uuid) VALUES(? ,?);", (insert) -> {
            insert.setInt(1, chatChannel.getId());
            insert.setString(2, player.getId().toString());
        });
    }

    public void removeMember(ChatChannel chatChannel, SppPlayer player) {
        query.create().deleteQuery("DELETE FROM sp_chat_channel_members WHERE channel_id=? AND player_uuid=?;", (insert) -> {
            insert.setInt(1, chatChannel.getId());
            insert.setString(2, player.getId().toString());
        });
    }

    public List<ChatChannel> findAll() {
        List<ChatChannel> chatChannels = query.create().find("SELECT * FROM sp_chat_channels", this::buildChatChannel);
        chatChannels.forEach(chatChannel -> chatChannel.setMembers(findChatChannelMembers(chatChannel.getId())));
        return chatChannels;
    }

    private ChatChannel buildChatChannel(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String chatPrefix = rs.getString("chat_prefix");
        String chatLine = rs.getString("chat_line");
        String channelId = rs.getString("channel_id");
        String channelName = rs.getString("channel_name");
        String serverName = rs.getString("server_name");
        ChatChannelType type = ChatChannelType.valueOf(rs.getString("type"));
        return new ChatChannel(
            id,
            chatPrefix,
            chatLine,
            channelName,
            channelId,
            type,
            serverName);
    }
}
