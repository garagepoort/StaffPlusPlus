package net.shortninja.staffplus.staff.mode;

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.session.SessionManager;
import org.bukkit.entity.Player;

public class StaffModeLuckPermsContextCalculator implements ContextCalculator<Player> {
    private static final String KEY = "staff++:staffmode";
    private final SessionManager sessionManager;

    public StaffModeLuckPermsContextCalculator() {
        sessionManager = IocContainer.getSessionManager();
    }

    @Override
    public void calculate(Player target, ContextConsumer consumer) {
        consumer.accept(KEY, Boolean.toString(sessionManager.get(target.getUniqueId()).inStaffChatMode()));
    }
}
