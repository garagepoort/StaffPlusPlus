package net.shortninja.staffplus.core.common.exceptions;

import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;

public class PlayerOfflineException extends BusinessException {
    public PlayerOfflineException() {
        super(StaffPlusPlus.get().getIocContainer().get(Messages.class).playerOffline);
    }
}
