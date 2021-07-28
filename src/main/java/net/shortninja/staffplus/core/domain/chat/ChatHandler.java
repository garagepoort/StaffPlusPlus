package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.configuration.ChatConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IocBean
public class ChatHandler {

    @ConfigProperty("permissions:chat-toggle")
    private String permissionChatToggle;
    @ConfigProperty("permissions:chat-slow")
    private String permissionChatSlow;

    private final static Map<UUID, Long> userChatTimes = new HashMap<>();
    private final PermissionHandler permission;
    private final ChatConfiguration chatConfiguration;

    private final Messages messages;
    private boolean isChatEnabled = true;
    private long chatSlowLength = 0;
    private long chatSlowStart = 0;

    public ChatHandler(PermissionHandler permission, ChatConfiguration chatConfiguration, Messages messages) {
        this.permission = permission;
        this.chatConfiguration = chatConfiguration;
        this.messages = messages;
    }

    public boolean isChatEnabled() {
        return isChatEnabled;
    }

    public boolean isChatEnabled(Player player) {
        return isChatEnabled || permission.has(player, permissionChatToggle);
    }

    public boolean canChat(Player player) {
        boolean canChat = true;

        if (chatSlowLength > 0 && !permission.has(player, permissionChatSlow)) {
            UUID uuid = player.getUniqueId();
            long now = System.currentTimeMillis();
            long lastChat = userChatTimes.containsKey(uuid) ? userChatTimes.get(uuid) : 0;

            if ((now - chatSlowStart) >= chatSlowLength) {
                chatSlowLength = 0;
                chatSlowStart = 0;
                userChatTimes.clear();
            } else if (((now - lastChat) / 1000) <= chatConfiguration.chatSlow) {
                canChat = false;
            } else userChatTimes.put(uuid, now);
        }

        return canChat;
    }

    public void setChatEnabled(String name, boolean isChatEnabled) {
        String status = isChatEnabled ? "enabled" : "disabled";

        messages.sendGlobalMessage(messages.chatToggled.replace("%status%", status).replace("%player%", name), messages.prefixGeneral);
        this.isChatEnabled = isChatEnabled;
    }

    public void setChatSlow(String name, int time) {
        chatSlowLength = time * 1000L;
        chatSlowStart = System.currentTimeMillis();
        messages.sendGlobalMessage(messages.chatSlowed.replace("%seconds%", Integer.toString(time)).replace("%player%", name), messages.prefixGeneral);
    }

    public void clearChat(String name) {
        for (int i = 0; i < chatConfiguration.chatLines; i++) {
            messages.sendGlobalMessage(messages.chatClearLine, "");
        }

        messages.sendGlobalMessage(messages.chatCleared.replace("%player%", name), messages.prefixGeneral);
    }
}