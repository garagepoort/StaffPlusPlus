package net.shortninja.staffplus.core.common.exceptions;

import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;

public class PlayerOfflineException extends BusinessException {
    public PlayerOfflineException() {
        super(TubingBukkitPlugin.getPlugin().getIocContainer().get(Messages.class).playerOffline);
    }
}
