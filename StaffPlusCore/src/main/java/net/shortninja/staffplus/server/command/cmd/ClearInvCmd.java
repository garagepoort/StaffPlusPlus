package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
<<<<<<< HEAD
import net.shortninja.staffplus.server.chat.ChatHandler;
=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ClearInvCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;

<<<<<<< HEAD
    public ClearInvCmd(String name){
=======
    public ClearInvCmd(String name) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
<<<<<<< HEAD
        if(args.length == 1){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(permission.has(player,options.permissionClearInv))
                if (Bukkit.getServer().getPlayer(args[0]) != null) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    JavaUtils.clearInventory(target);
                    sender.sendMessage(target.getName() + "'s inventory has been cleared");
                    return true;
                }
            }
        }else
=======
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (permission.has(player, options.permissionClearInv))
                    if (Bukkit.getServer().getPlayer(args[0]) != null) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        JavaUtils.clearInventory(target);
                        sender.sendMessage(target.getName() + "'s inventory has been cleared");
                        return true;
                    }
            }
        } else
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
            sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}
