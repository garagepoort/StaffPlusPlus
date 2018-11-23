package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.chat.ChatHandler;
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
    private ChatHandler chatHandler = StaffPlus.get().chatHandler;

    public ClearInvCmd(String name){
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(args.length == 1){
            if(Bukkit.getServer().getPlayer(args[0]) != null){
                Player p = Bukkit.getServer().getPlayer(args[0]);
                JavaUtils.clearInventory(p);
                sender.sendMessage(p.getName()+"'s inventory has been cleared");
                return true;
            }
        }else
            sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}
