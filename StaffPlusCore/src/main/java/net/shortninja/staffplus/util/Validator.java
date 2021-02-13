package net.shortninja.staffplus.util;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class Validator {

    private final PermissionHandler permissionHandler = IocContainer.getPermissionHandler();

    private final Player player;

    private Validator(Player player) {
        this.player = player;
    }

    public static Validator validator(Player player) {
        return new Validator(player);
    }

    public Validator validatePermission(String permission) {
        this.permissionHandler.validate(player, permission);
        return this;
    }

    public Validator validateNotEmpty(String value, String errorMessage) {
        if (StringUtils.isEmpty(value)) {
            throw new BusinessException(errorMessage);
        }
        return this;
    }
}
