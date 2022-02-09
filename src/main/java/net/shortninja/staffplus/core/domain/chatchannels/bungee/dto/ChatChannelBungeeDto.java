package net.shortninja.staffplus.core.domain.chatchannels.bungee.dto;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.chatchannels.IChatChannel;

import java.util.Set;
import java.util.UUID;

public class ChatChannelBungeeDto extends BungeeMessage {

    private final int id;
    private final String prefix;
    private final String line;
    private final String name;
    private final String channelId;
    private final ChatChannelType type;
    private final Set<UUID> members;

    public ChatChannelBungeeDto(IChatChannel channel) {
        super(channel.getServerName());
        this.id = channel.getId();
        this.prefix = channel.getPrefix();
        this.line = channel.getLine();
        this.name = channel.getName();
        this.channelId = channel.getChannelId();
        this.type = channel.getType();
        this.members = channel.getMembers();
    }

    public int getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    public String getChannelId() {
        return channelId;
    }

    public ChatChannelType getType() {
        return type;
    }

    public Set<UUID> getMembers() {
        return members;
    }
}
