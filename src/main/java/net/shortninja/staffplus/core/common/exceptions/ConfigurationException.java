package net.shortninja.staffplus.core.common.exceptions;

public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super("Invalid S++ configuration: [" + message + "]");
    }

    public ConfigurationException(String message, Throwable e) {
        super(message, e);
    }
}
