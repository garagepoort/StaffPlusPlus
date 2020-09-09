package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class StaffPlusCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;

    public StaffPlusCmd(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!permission.has(sender, options.permissionStaff)) {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return true;
        }
        if (args.length == 1 && permission.has(sender, options.permissionStaff)) {
            if (args[0].equalsIgnoreCase("reload")) {
                Bukkit.getPluginManager().getPlugin("StaffPlus").reloadConfig();
                StaffPlus.get().reloadFiles();
                StaffPlus.get().message.sendConsoleMessage("Plugin config and lang file reloaded", false);
                if (sender instanceof Player) {
                    StaffPlus.get().message.send((Player) sender, "Config and lang file have been reloaded", "StaffPlus");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            return Collections.singletonList("reload");
        }

        return super.tabComplete(sender, alias, args);
    }
}
