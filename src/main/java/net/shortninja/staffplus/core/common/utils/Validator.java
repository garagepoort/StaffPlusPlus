package net.shortninja.staffplus.core.common.utils;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class Validator {

    private final PermissionHandler permissionHandler = StaffPlus.get().getIocContainer().get(PermissionHandler.class);

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

    public Validator validateAnyPermission(String... permissions) {
        if (!this.permissionHandler.hasAny(player, permissions)) {
            throw new NoPermissionException();
        }
        return this;
    }

    public Validator validateNotEmpty(String value, String errorMessage) {
        if (StringUtils.isEmpty(value)) {
            throw new BusinessException(errorMessage);
        }
        return this;
    }
}
