package net.shortninja.staffplus.common.exceptions;

import net.shortninja.staffplus.application.IocContainer;

public class PlayerOfflineException extends BusinessException {
    public PlayerOfflineException() {
        super(IocContainer.getMessages().playerOffline);
    }
}
