package net.shortninja.staffplus.server.chat;

import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatHandler {
    private final static Map<UUID, Long> userChatTimes = new HashMap<UUID, Long>();
    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private boolean isChatEnabled = true;
    private long chatSlowLength = 0;
    private long chatSlowStart = 0;

    public ChatHandler(PermissionHandler permission, MessageCoordinator message, Options options, Messages messages) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
    }

    public boolean isChatEnabled() {
        return isChatEnabled;
    }

    public boolean isChatEnabled(Player player) {
        return isChatEnabled || permission.has(player, options.permissionChatToggle);
    }

    public boolean canChat(Player player) {
        boolean canChat = true;

        if (chatSlowLength > 0 && !permission.has(player, options.permissionChatSlow)) {
            UUID uuid = player.getUniqueId();
            long now = System.currentTimeMillis();
            long lastChat = userChatTimes.containsKey(uuid) ? userChatTimes.get(uuid) : 0;

            if ((now - chatSlowStart) >= chatSlowLength) {
                chatSlowLength = 0;
                chatSlowStart = 0;
                userChatTimes.clear();
            } else if (((now - lastChat) / 1000) <= options.chatConfiguration.getChatSlow()) {
                canChat = false;
            } else userChatTimes.put(uuid, now);
        }

        return canChat;
    }

    public void setChatEnabled(String name, boolean isChatEnabled) {
        String status = isChatEnabled ? "enabled" : "disabled";

        message.sendGlobalMessage(messages.chatToggled.replace("%status%", status).replace("%player%", name), messages.prefixGeneral);
        this.isChatEnabled = isChatEnabled;
    }

    public void setChatSlow(String name, int time) {
        chatSlowLength = time * 1000;
        chatSlowStart = System.currentTimeMillis();
        message.sendGlobalMessage(messages.chatSlowed.replace("%seconds%", Integer.toString(time)).replace("%player%", name), messages.prefixGeneral);
    }

    public void clearChat(String name) {
        for (int i = 0; i < options.chatConfiguration.getChatLines(); i++) {
            message.sendGlobalMessage(messages.chatClearLine, "");
        }

        message.sendGlobalMessage(messages.chatCleared.replace("%player%", name), messages.prefixGeneral);
    }
}