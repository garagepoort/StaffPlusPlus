package net.shortninja.staffplus.core.common.exceptions;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(IocContainer.get(Messages.class).noPermission, prefix);
    }

    public NoPermissionException() {
        super(IocContainer.get(Messages.class).noPermission);
    }
}
