package net.shortninja.staffplus.core.domain.chatchannels;

import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.chatchannels.IChatChannel;

import java.util.List;
import java.util.UUID;

public class ChatChannel implements IChatChannel {

    private int id;
    private final String channelId;
    private final List<UUID> members;
    private final ChatChannelType type;
    private final String serverName;

    public ChatChannel(int id, String channelId, List<UUID> members, ChatChannelType type, String serverName) {
        this.id = id;
        this.channelId = channelId;
        this.members = members;
        this.type = type;
        this.serverName = serverName;
    }

    public ChatChannel(String channelId, List<UUID> members, ChatChannelType type, String serverName) {
        this.channelId = channelId;
        this.members = members;
        this.type = type;
        this.serverName = serverName;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public int getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getServerName() {
        return serverName;
    }

    public ChatChannelType getType() {
        return type;
    }
}
