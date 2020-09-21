package net.shortninja.staffplus.staff.vanish;

import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;

public class VanishChatPreventer implements ChatPreventer {

    private final VanishHandler vanishHandler;
    private final Options options;
    private final MessageCoordinator message;
    private final Messages messages;

    public VanishChatPreventer(VanishHandler vanishHandler, Options options, MessageCoordinator message, Messages messages) {
        this.vanishHandler = vanishHandler;
        this.options = options;
        this.message = message;
        this.messages = messages;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        if (options.vanishEnabled && !options.vanishChatEnabled && vanishHandler.isVanished(player)) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
