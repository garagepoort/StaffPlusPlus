package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;

public class BusinessException extends RuntimeException {
    private final String prefix;

    public BusinessException(String message) {
        super(message);
        this.prefix = IocContainer.get(Messages.class).prefixGeneral;
    }

    public BusinessException(String message, String prefix) {
        super(message);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
