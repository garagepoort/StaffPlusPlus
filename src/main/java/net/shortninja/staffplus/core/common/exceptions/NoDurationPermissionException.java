package net.shortninja.staffplus.core.common.exceptions;

public class NoDurationPermissionException extends BusinessException {

    public NoDurationPermissionException() {
        super("&CYou are not allowed to use this command with this duration");
    }
}
