package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(IocContainer.get(Messages.class).noPermission, prefix);
    }

    public NoPermissionException() {
        super(IocContainer.get(Messages.class).noPermission);
    }
}
