package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;

public class BusinessException extends RuntimeException {
    private final String prefix;

    public BusinessException(String message) {
        super(message);
        this.prefix = StaffPlusPlus.get().getIocContainer().get(Messages.class).prefixGeneral;
    }

    public BusinessException(String message, String prefix) {
        super(message);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
