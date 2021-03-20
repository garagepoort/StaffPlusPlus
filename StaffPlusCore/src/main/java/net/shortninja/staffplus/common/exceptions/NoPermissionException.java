package net.shortninja.staffplus.common.exceptions;

import net.shortninja.staffplus.application.IocContainer;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(IocContainer.getMessages().noPermission, prefix);
    }

    public NoPermissionException() {
        super(IocContainer.getMessages().noPermission);
    }
}
