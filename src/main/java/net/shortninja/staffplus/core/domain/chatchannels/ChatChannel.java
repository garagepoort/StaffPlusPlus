package net.shortninja.staffplus.core.domain.chatchannels;

import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.chatchannels.IChatChannel;
import net.shortninja.staffplusplus.session.SppInteractor;

import java.util.Set;
import java.util.UUID;

public class ChatChannel implements IChatChannel {

    private int id;
    private final String prefix;
    private final String line;
    private final String name;
    private final String channelId;
    private final Set<UUID> members;
    private final ChatChannelType type;
    private final String serverName;

    public ChatChannel(int id, String prefix, String line, String name, String channelId, Set<UUID> members, ChatChannelType type, String serverName) {
        this.id = id;
        this.prefix = prefix;
        this.line = line;
        this.name = name;
        this.channelId = channelId;
        this.members = members;
        this.type = type;
        this.serverName = serverName;
    }

    public ChatChannel(String prefix, String line, String channelId, Set<UUID> members, ChatChannelType type, String serverName) {
        this.prefix = prefix;
        this.line = line;
        this.name = type.name() + "_" + channelId;
        this.channelId = channelId;
        this.members = members;
        this.type = type;
        this.serverName = serverName;
    }

    public Set<UUID> getMembers() {
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

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLine() {
        return line;
    }

    public void addMember(UUID uniqueId) {
        members.add(uniqueId);
    }

    public void removeMember(UUID uniqueId) {
        members.remove(uniqueId);
    }

    public boolean hasMember(SppInteractor player) {
        return !player.isBukkitPlayer() || members.contains(player.getId());
    }
}
