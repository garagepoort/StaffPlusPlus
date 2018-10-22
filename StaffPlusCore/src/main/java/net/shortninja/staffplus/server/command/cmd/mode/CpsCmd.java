package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.handler.GadgetHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CpsCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;

    public CpsCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        Player targetPlayer = null;

        if (!permission.has(sender, options.permissionCps)) {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return true;
        }

        if (args.length == 1) {
            targetPlayer = Bukkit.getPlayer(args[0]);
        } else if (!(sender instanceof Player)) {
            message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
            return true;
        } else targetPlayer = JavaUtils.getTargetPlayer((Player) sender);

        if (targetPlayer != null) {
            gadgetHandler.onCps(sender, targetPlayer);
        } else message.send(sender, messages.playerOffline, messages.prefixGeneral);

        return true;
    }
}