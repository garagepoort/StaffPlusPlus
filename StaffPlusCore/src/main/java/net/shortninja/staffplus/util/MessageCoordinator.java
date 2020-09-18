package net.shortninja.staffplus.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.IStaffPlus;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.lib.Message;
import net.shortninja.staffplus.util.lib.hex.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MessageCoordinator extends Message {
    public final String LONG_LINE = "&m" + Strings.repeat('-', 48);
    private PermissionHandler permission;

    public MessageCoordinator(IStaffPlus staffPlus, PermissionHandler permission) {
        super(staffPlus);
        this.permission = permission;
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void send(Player player, String message, String prefix) {
        if (player == null || message.isEmpty()) {
            return;
        }
        if (StaffPlus.get().usesPlaceholderAPI) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        if (!prefix.equals("")) {
            player.sendMessage(colorize(prefix + " " + message));

        } else {
            player.sendMessage(colorize(prefix + "" + message));
        }
    }

    public void send(Player player, String message, String prefix, String permission) {
        if (player == null || message == null || message.isEmpty() || !this.permission.has(player, permission)) {
            return;
        }

        if (StaffPlus.get().usesPlaceholderAPI) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        if (!prefix.equals(""))
            player.sendMessage(colorize(prefix + " " + message));
        else
            player.sendMessage(colorize(prefix + "" + message));
    }

    public void send(CommandSender sender, String message, String prefix) {
        if (sender == null || message.isEmpty()) {
            return;
        }


        if (sender instanceof Player) {
            if (StaffPlus.get().usesPlaceholderAPI) {
                message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            }
        }

        if (!prefix.equals(""))
            sender.sendMessage(colorize(prefix + " " + message));
        else
            sender.sendMessage(colorize(prefix + "" + message));
    }

    public void sendConsoleMessage(String message, boolean isError) {
        String prefix = isError ? "&4[Staff+] &c" : "&2[Staff+] &a";

        Bukkit.getServer().getConsoleSender().sendMessage(colorize(prefix + message));
    }

    public void sendGlobalMessage(String message, String prefix) {
        if (message.isEmpty() && !prefix.isEmpty()) {
            return;
        }
        if (!prefix.equals(""))
            Bukkit.broadcastMessage(colorize(prefix + " " + message));
        else
            Bukkit.broadcastMessage(colorize(prefix + "" + message));
    }

    public void sendGroupMessage(String message, String permission, String prefix) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (StaffPlus.get().usesPlaceholderAPI) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }

            send(player, message, prefix, permission);
        }
    }

    public void sendCollectedMessage(Player player, Collection<String> messages, String prefix) {
        for (String message : messages) {
            if (StaffPlus.get().usesPlaceholderAPI) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }

            send(player, message, prefix);
        }
    }
}