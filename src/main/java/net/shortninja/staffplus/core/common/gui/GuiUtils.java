package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.entity.Player;

public class GuiUtils {

    public static boolean hasPermission(Player player, String permission) {
        return StaffPlus.get().getIocContainer().get(PermissionHandler.class).has(player, permission);
    }

    public static String getPreviousPage(String currentAction, int currentPage) {
        return GuiActionBuilder.fromAction(currentAction)
            .param("page", String.valueOf((currentPage - 1)))
            .build();
    }

    public static String getNextPage(String currentAction, int currentPage) {
        return GuiActionBuilder.fromAction(currentAction)
            .param("page", String.valueOf((currentPage + 1)))
            .build();
    }
}
