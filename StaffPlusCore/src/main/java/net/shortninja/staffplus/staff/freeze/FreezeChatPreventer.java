package net.shortninja.staffplus.staff.freeze;

import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;

public class FreezeChatPreventer implements ChatPreventer {


    private final FreezeHandler freezeHandler;
    private final Options options;
    private final Messages messages;
    private final MessageCoordinator message;

    public FreezeChatPreventer(FreezeHandler freezeHandler, Options options, Messages messages, MessageCoordinator message) {
        this.freezeHandler = freezeHandler;
        this.options = options;
        this.messages = messages;
        this.message = message;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        if (freezeHandler.isFrozen(player.getUniqueId()) && !options.modeFreezeChat) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
