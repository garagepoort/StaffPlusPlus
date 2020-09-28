package net.shortninja.staffplus;

import net.shortninja.staffplus.server.data.config.IOptions;
import net.shortninja.staffplus.util.IPermissionsHandler;

import java.util.UUID;

public interface IStaffPlus {

    static IStaffPlus get() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    IOptions getOptions();

    IPermissionsHandler getPermissions();

    boolean isPlayerVanished(UUID playerUuid);

}
