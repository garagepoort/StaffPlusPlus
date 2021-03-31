package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;

public class BusinessException extends RuntimeException {
    private final String prefix;

    public BusinessException(String message) {
        super(message);
        this.prefix = StaffPlus.get().getIocContainer().get(Messages.class).prefixGeneral;
    }

    public BusinessException(String message, String prefix) {
        super(message);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
