package net.shortninja.staffplus.common.exceptions;

import net.shortninja.staffplus.application.IocContainer;

public class BusinessException extends RuntimeException {
    private final String prefix;

    public BusinessException(String message) {
        super(message);
        this.prefix = IocContainer.getMessages().prefixGeneral;
    }

    public BusinessException(String message, String prefix) {
        super(message);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
