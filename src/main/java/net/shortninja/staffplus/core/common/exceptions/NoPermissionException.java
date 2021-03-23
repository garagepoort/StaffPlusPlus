package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(StaffPlus.get().iocContainer.get(Messages.class).noPermission, prefix);
    }

    public NoPermissionException() {
        super(StaffPlus.get().iocContainer.get(Messages.class).noPermission);
    }
}
