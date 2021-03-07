package net.shortninja.staffplus.common.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;

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
