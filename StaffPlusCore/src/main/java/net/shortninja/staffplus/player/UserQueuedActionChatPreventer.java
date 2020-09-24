package net.shortninja.staffplus.player;

import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IPlayerSession;
import org.bukkit.entity.Player;

public class UserQueuedActionChatPreventer implements ChatPreventer {
    private final SessionManager sessionManager;

    public UserQueuedActionChatPreventer(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        IPlayerSession session = sessionManager.get(player.getUniqueId());
        IAction queuedAction = session.getQueuedAction();

        if (queuedAction != null) {
            queuedAction.execute(player, message);
            session.setQueuedAction(null);
            return true;
        }
        return false;
    }
}
