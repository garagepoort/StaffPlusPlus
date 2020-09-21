package net.shortninja.staffplus.server.chat;

import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;

public class GeneralChatPreventer implements ChatPreventer{

    private final ChatHandler chatHandler;
    private final MessageCoordinator message;
    private final Messages messages;

    public GeneralChatPreventer(ChatHandler chatHandler, MessageCoordinator message, Messages messages) {
        this.chatHandler = chatHandler;
        this.message = message;
        this.messages = messages;
    }

    public boolean shouldPrevent(Player player, String message){
        if (!chatHandler.canChat(player)) {
            this.message.send(player, messages.chattingFast, messages.prefixGeneral);
            return true;
        }

        if (!chatHandler.isChatEnabled(player)) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
            return true;
        }

        return false;
    }
}
