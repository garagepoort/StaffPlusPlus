package net.shortninja.staffplus.core.domain.staff.mode;

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.entity.Player;

public class StaffModeLuckPermsContextCalculator implements ContextCalculator<Player> {
    private static final String KEY = "staff++:staffmode";
    private final SessionManagerImpl sessionManager;

    public StaffModeLuckPermsContextCalculator(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void calculate(Player target, ContextConsumer consumer) {
        if (target.isOnline()) {
            if (sessionManager.has(target.getUniqueId())) {
                consumer.accept(KEY, Boolean.toString(sessionManager.get(target.getUniqueId()).isInStaffMode()));
            } else {
                consumer.accept(KEY, String.valueOf(false));
            }
        }
    }
}
