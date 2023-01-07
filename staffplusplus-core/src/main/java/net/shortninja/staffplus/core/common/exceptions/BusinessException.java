package net.shortninja.staffplus.core.common.exceptions;

import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;

public class BusinessException extends RuntimeException {
    private final String prefix;

    public BusinessException(String message) {
        super(message);
        this.prefix = TubingBukkitPlugin.getPlugin().getIocContainer().get(Messages.class).prefixGeneral;
    }

    public BusinessException(String message, String prefix) {
        super(message);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
