package net.shortninja.staffplus.server.command.cmd.security;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.SecurityHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.listener.player.PlayerJoin;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ChangePassCmd extends BukkitCommand {

    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;

    public ChangePassCmd(String name){
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (PlayerJoin.needLogin.contains(p.getUniqueId())){
                message.send(p,messages.loginWaiting,messages.prefixGeneral);
                return true;
            }else if(!securityHandler.hasPassword(p.getUniqueId())){
                message.send(p,messages.loginRegister,messages.prefixGeneral);
                return true;
            }
            // /changepass <pass> <confirm>
            if(args.length >= 2){
                String newPass = args[0];
                String confirmPass = args[1];
                if(newPass.equals(confirmPass)){
                    securityHandler.setPassword(p.getUniqueId(),newPass,true);
                    message.send(p,messages.loginRegistered,messages.prefixGeneral);
                }else
                    message.send(p,messages.passwordsNoMatch,messages.prefixGeneral);
            }else
                message.send(p, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
        }else
            message.send(sender,"Only players can use this command",messages.prefixGeneral);
        return true;
    }
}
