package net.shortninja.staffplus;

import net.shortninja.staffplus.server.data.config.IOptions;
import net.shortninja.staffplus.unordered.ISessionManager;
import net.shortninja.staffplus.util.IPermissionsHandler;

public interface IStaffPlus {

    static IStaffPlus get() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    IOptions getOptions();

    IPermissionsHandler getPermissions();

    ISessionManager getUserManager();

}
