package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(StaffPlusPlus.get().getIocContainer().get(Messages.class).noPermission, prefix);
    }

    public NoPermissionException() {
        super(StaffPlusPlus.get().getIocContainer().get(Messages.class).noPermission);
    }
}
