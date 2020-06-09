package net.shortninja.staffplus.server.command.cmd.security;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.SecurityHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ResetPassCmd extends BukkitCommand {

    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;

<<<<<<< HEAD
    public ResetPassCmd(String name){
=======
    public ResetPassCmd(String name) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!permission.has(sender, options.permissionMember)) {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return true;
        } else if (!(sender instanceof Player)) {
            message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
            return true;
        } else if (args.length != 2) {  //reset <user> <password>
            message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
            return true;
        }
<<<<<<< HEAD
        if(permission.hasOnly((Player)sender,options.permissionResetPassword)){
            UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
            securityHandler.setPassword(Bukkit.getPlayer(uuid),args[1].getBytes(StandardCharsets.UTF_8));
            return true;
        }else{
=======
        if (permission.hasOnly((Player) sender, options.permissionResetPassword)) {
            UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
            securityHandler.setPassword(Bukkit.getPlayer(uuid), args[1].getBytes(StandardCharsets.UTF_8));
            return true;
        } else {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
            message.send(sender, messages.noPermission, messages.prefixGeneral);
        }
        return false;
    }


}
