package net.shortninja.staffplus.server.command.cmd.security;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.SecurityHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.listener.player.PlayerJoin;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class LoginCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;

    public LoginCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!permission.has(sender, options.permissionMember)) {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return true;
        } else if (!(sender instanceof Player)) {
            message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
            return true;
        } else if (args.length == 0) {
            message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();


        if (securityHandler.hasPassword(player)) {
            if (securityHandler.isPasswordMatch(args[0].getBytes(StandardCharsets.UTF_8),securityHandler.getPassword(player))) {
                freezeHandler.removeFreeze(player, player, false);
                PlayerJoin.needLogin.remove(player.getUniqueId());
                message.send(sender, messages.loginAccepted, messages.prefixGeneral);
            } else {
                freezeHandler.removeFreeze(player,player,false);
                player.kickPlayer(message.colorize(options.loginKick));
            }
        } else message.send(sender, messages.loginRegister, messages.prefixGeneral);

        return true;
    }
}