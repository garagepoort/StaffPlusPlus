package net.shortninja.staffplus.domain.actions;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.SppPlayer;

public class PermissionActionFilter implements ActionFilter {

    private static final String PERMISSION = "permission";

    @Override
    public boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction) {
        if (configuredAction.getFilters().containsKey(PERMISSION)) {
            if(!target.isOnline()) {
                return false;
            }
            String permission = configuredAction.getFilters().get(PERMISSION);
            return IocContainer.getPermissionHandler().has(target.getPlayer(), permission);
        }
        return true;
    }

}
