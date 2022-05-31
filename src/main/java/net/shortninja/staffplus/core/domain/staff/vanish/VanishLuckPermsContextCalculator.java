package net.shortninja.staffplus.core.domain.staff.vanish;

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;

public class VanishLuckPermsContextCalculator implements ContextCalculator<Player> {
    private static final String KEY = "staff++:vanished";
    private final OnlineSessionsManager sessionManager;

    public VanishLuckPermsContextCalculator(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void calculate(Player target, ContextConsumer consumer) {
        if (target.isOnline()) {
            if (sessionManager.has(target.getUniqueId())) {
                consumer.accept(KEY, Boolean.toString(sessionManager.get(target).isVanished()));
            } else {
                consumer.accept(KEY, String.valueOf(false));
            }
        }
    }
}
