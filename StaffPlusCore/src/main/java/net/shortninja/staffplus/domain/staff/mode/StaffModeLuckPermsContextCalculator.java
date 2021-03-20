package net.shortninja.staffplus.domain.staff.mode;

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.entity.Player;

public class StaffModeLuckPermsContextCalculator implements ContextCalculator<Player> {
    private static final String KEY = "staff++:staffmode";
    private final SessionManagerImpl sessionManager;

    public StaffModeLuckPermsContextCalculator() {
        sessionManager = IocContainer.getSessionManager();
    }

    @Override
    public void calculate(Player target, ContextConsumer consumer) {
        if(target.isOnline()) {
            consumer.accept(KEY, Boolean.toString(sessionManager.get(target.getUniqueId()).isInStaffMode()));
        }
    }
}
