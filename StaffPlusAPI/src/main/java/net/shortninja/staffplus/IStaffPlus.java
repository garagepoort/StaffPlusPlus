package net.shortninja.staffplus;

import net.shortninja.staffplus.server.data.config.IOptions;
import net.shortninja.staffplus.util.IPermissionsHandler;

public interface IStaffPlus {

    IOptions getOptions();

    IPermissionsHandler getPermissions();
    static IStaffPlus get() {
        throw new UnsupportedOperationException("Not implemented!");
    }



}
