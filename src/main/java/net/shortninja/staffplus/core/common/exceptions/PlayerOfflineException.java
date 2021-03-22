package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;

public class PlayerOfflineException extends BusinessException {
    public PlayerOfflineException() {
        super(IocContainer.get(Messages.class).playerOffline);
    }
}
