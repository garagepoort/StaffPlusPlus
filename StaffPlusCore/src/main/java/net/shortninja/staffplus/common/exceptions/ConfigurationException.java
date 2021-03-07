package net.shortninja.staffplus.common.exceptions;

public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super("Invalid S++ configuration: [" + message + "]");
    }
}
