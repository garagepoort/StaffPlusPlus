package net.shortninja.staffplus.core.common.time;

import net.shortninja.staffplus.core.common.exceptions.BusinessException;

public class TimeParsingException extends BusinessException {
    public TimeParsingException(String message) {
        super(message);
    }
}
