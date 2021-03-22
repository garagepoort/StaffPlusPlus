package net.shortninja.staffplus.core.common.exceptions;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;

public class PlayerOfflineException extends BusinessException {
    public PlayerOfflineException() {
        super(IocContainer.get(Messages.class).playerOffline);
    }
}
