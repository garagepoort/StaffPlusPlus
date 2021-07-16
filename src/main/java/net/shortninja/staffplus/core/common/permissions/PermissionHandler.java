package net.shortninja.staffplus.core.common.permissions;

import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.time.TimeUnitShort;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface PermissionHandler {

    default List<String> getPermissions(CommandSender player) {
        return player.getEffectivePermissions().stream()
            .map(PermissionAttachmentInfo::getPermission)
            .collect(Collectors.toList());
    }

    default Optional<Long> getDurationInSeconds(CommandSender player, String permission) {
        return getPermissions(player).stream()
            .filter(p -> p.startsWith(permission + "."))
            .map(p -> TimeUnitShort.getDurationFromString(p.substring(p.lastIndexOf(".") + 1)) * 1000)
            .max(Comparator.naturalOrder());
    }

    boolean has(Player player, String permission);

    boolean has(OfflinePlayer player, String permission);

    boolean hasAny(CommandSender player, String... permissions);

    boolean hasAny(CommandSender player, Set<String> permissions);

    void validate(CommandSender player, String permission);

    void validateAny(CommandSender player, Set<String> permissions);

    boolean hasOnly(Player player, String permission);

    boolean has(CommandSender sender, String permission);

    default boolean isOp(CommandSender sender) {
        return sender.isOp();
    }

    int getStaffCount();

    default void validateOp(CommandSender sender) {
        if (!sender.isOp()) {
            throw new NoPermissionException();
        }
    }
}