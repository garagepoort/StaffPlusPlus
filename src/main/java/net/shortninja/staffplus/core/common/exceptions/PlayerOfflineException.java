package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;

public class PlayerOfflineException extends BusinessException {
    public PlayerOfflineException() {
        super(StaffPlus.get().getIocContainer().get(Messages.class).playerOffline);
    }
}
