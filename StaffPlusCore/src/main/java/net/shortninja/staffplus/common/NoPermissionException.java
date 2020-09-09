package net.shortninja.staffplus.common;

import net.shortninja.staffplus.StaffPlus;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(StaffPlus.get().messages.noPermission, prefix);
    }
}
