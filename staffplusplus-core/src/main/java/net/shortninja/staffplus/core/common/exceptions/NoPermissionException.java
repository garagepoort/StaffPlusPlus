package net.shortninja.staffplus.core.common.exceptions;

import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;

public class NoPermissionException extends BusinessException {
    public NoPermissionException(String prefix) {
        super(TubingBukkitPlugin.getPlugin().getIocContainer().get(Messages.class).noPermission, prefix);
    }

    public NoPermissionException() {
        super(TubingBukkitPlugin.getPlugin().getIocContainer().get(Messages.class).noPermission);
    }
}
