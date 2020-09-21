package net.shortninja.staffplus.player;

import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IUser;
import org.bukkit.entity.Player;

public class UserQueuedActionChatPreventer implements ChatPreventer {
    private final UserManager userManager;

    public UserQueuedActionChatPreventer(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        IUser user = userManager.get(player.getUniqueId());
        IAction queuedAction = user.getQueuedAction();

        if (queuedAction != null) {
            queuedAction.execute(player, message);
            user.setQueuedAction(null);
            return true;
        }
        return false;
    }
}
