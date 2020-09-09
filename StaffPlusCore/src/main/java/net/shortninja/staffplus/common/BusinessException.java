package net.shortninja.staffplus.common;

public class BusinessException extends RuntimeException {
    private String prefix;

    public BusinessException(String message, String prefix) {
        super(message);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
