package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;

public class PermissionActionFilter implements ActionFilter {

    private static final String PERMISSION = "permission";

    @Override
    public boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction) {
        if (configuredAction.getFilters().containsKey(PERMISSION)) {
            if(!target.isOnline()) {
                return false;
            }
            String permission = configuredAction.getFilters().get(PERMISSION);
            return IocContainer.get(PermissionHandler.class).has(target.getPlayer(), permission);
        }
        return true;
    }

}
