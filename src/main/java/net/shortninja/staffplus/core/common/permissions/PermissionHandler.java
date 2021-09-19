package net.shortninja.staffplus.core.common.permissions;

import be.garagepoort.mcioc.permissions.TubingPermissionService;
import net.shortninja.staffplus.core.common.exceptions.NoDurationPermissionException;
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

public interface PermissionHandler extends TubingPermissionService {

    default List<String> getPermissions(CommandSender player) {
        return player.getEffectivePermissions().stream()
            .map(PermissionAttachmentInfo::getPermission)
            .collect(Collectors.toList());
    }

    default Optional<Long> getDurationInSeconds(CommandSender player, String permission) {
        return getDurationInMillis(player, permission).map(d -> d / 1000);
    }

    default Optional<Long> getDurationInMillis(CommandSender player, String permission) {
        return getPermissions(player).stream()
            .filter(p -> p.startsWith(permission + "."))
            .map(p -> TimeUnitShort.getDurationFromString(p.substring(p.lastIndexOf(".") + 1)))
            .max(Comparator.naturalOrder());
    }

    @Override
    boolean has(Player player, String permission);

    boolean has(OfflinePlayer player, String permission);

    boolean hasAny(CommandSender player, String... permissions);

    boolean hasAny(CommandSender player, Set<String> permissions);

    void validate(CommandSender player, String permission);

    void validateAny(CommandSender player, Set<String> permissions);

    boolean hasOnly(Player player, String permission);

    boolean has(CommandSender sender, String permission);

    int getStaffCount();

    default void validateOp(CommandSender sender) {
        if (!sender.isOp()) {
            throw new NoPermissionException();
        }
    }

    default void validateDuration(CommandSender player, String permission, long durationInMillis) {
        if (!(player instanceof Player)) {
            return;
        }

        List<String> permissions = this.getPermissions(player);
        if (permissions.stream().noneMatch(p -> p.startsWith(permission))) {
            return;
        }
        Optional<Long> duration = this.getDurationInMillis(player, permission);

        if (duration.isPresent() && duration.get() < durationInMillis) {
            throw new NoDurationPermissionException();
        }
    }

}