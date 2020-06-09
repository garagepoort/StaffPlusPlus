package net.shortninja.staffplus;

import net.shortninja.staffplus.server.data.config.IOptions;
import net.shortninja.staffplus.unordered.IUserManager;
import net.shortninja.staffplus.util.IPermissionsHandler;

public interface IStaffPlus {

    static IStaffPlus get() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    IOptions getOptions();

    IPermissionsHandler getPermissions();

    IUserManager getUserManager();
<<<<<<< HEAD
=======


>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
}
