package net.shortninja.staffplus.common;

import net.shortninja.staffplus.IocContainer;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(IocContainer.getMessages().noPermission, prefix);
    }
}
