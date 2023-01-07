package net.shortninja.staffplus.core.domain.player.settings;

import net.shortninja.staffplus.core.common.exceptions.BusinessException;

public class PlayerSettingCreationException extends BusinessException {
    public PlayerSettingCreationException(String message) {
        super(message);
    }
}
