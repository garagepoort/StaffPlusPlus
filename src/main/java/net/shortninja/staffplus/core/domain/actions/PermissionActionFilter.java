package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Map;
import java.util.Optional;

@IocBean
public class PermissionActionFilter implements ActionFilter {

    private static final String PERMISSION = "permission";

    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;

    public PermissionActionFilter(PermissionHandler permissionHandler, PlayerManager playerManager) {
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
    }

    @Override
    public boolean isValidAction(CreateStoredCommandRequest createStoredCommandRequest, Map<String, String> filters) {
        if (filters.containsKey(PERMISSION)) {
            if (createStoredCommandRequest.getExecutioner().equals(Constants.CONSOLE_UUID)) {
                return true;
            }

            Optional<SppPlayer> onlinePlayer = playerManager.getOnlinePlayer(createStoredCommandRequest.getExecutioner());
            if (onlinePlayer.isPresent()) {
                String permission = filters.get(PERMISSION);
                return permissionHandler.has(onlinePlayer.get().getPlayer(), permission);
            }

        }
        return true;
    }
}
