package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

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

    public static String parseTimestamp(long timestamp, String format) {
        LocalDateTime localDateTime = getLocalDateTime(timestamp);
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime getLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }
}
