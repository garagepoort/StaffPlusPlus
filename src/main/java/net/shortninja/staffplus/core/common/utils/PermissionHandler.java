package net.shortninja.staffplus.core.common.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public interface PermissionHandler {

    boolean has(Player player, String permission);

    boolean hasAny(CommandSender player, String... permissions);

    boolean hasAny(CommandSender player, Set<String> permissions);

    void validate(CommandSender player, String permission);

    void validateAny(CommandSender player, Set<String> permissions);

    boolean hasOnly(Player player, String permission);

    boolean has(CommandSender sender, String permission);

    boolean isOp(Player player);

    boolean isOp(CommandSender sender);

    int getStaffCount();
}