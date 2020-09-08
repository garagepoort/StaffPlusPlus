package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.warn.WarnService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.List;

public class WarnCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;

    public WarnCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!permission.has(sender, options.permissionWarn)) {
            message.send(sender, messages.noPermission, messages.prefixWarnings);
            return true;
        }

        if (args.length == 2) {
            String argument = args[0];
            String playerName = args[1];

            if (argument.equalsIgnoreCase("get")) {
                listWarnings(sender, playerName);
            } else if (argument.equalsIgnoreCase("clear")) {
                clearWarnings(sender, playerName);
            } else sendWarning(sender, argument, JavaUtils.compileWords(args, 1));
        } else if (args.length >= 3) {
            sendWarning(sender, args[0], JavaUtils.compileWords(args, 1));
        } else if (!permission.has(sender, options.permissionWarn)) {
            message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixWarnings);
        } else sendHelp(sender);

        return true;
    }

    private void listWarnings(CommandSender sender, String playerName) {
        IUser user = userManager.getOnOrOfflineUser(playerName);

        if (user != null) {
            List<IWarning> warnings = user.getWarnings();

            for (String message : messages.warningsListStart) {
                this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", playerName).replace("%warnings%", Integer.toString(warnings.size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
            }

            for (int i = 0; i < warnings.size(); i++) {
                IWarning warning = warnings.get(i);

                message.send(sender, messages.warningsListEntry.replace("%count%", Integer.toString(i + 1)).replace("%reason%", warning.getReason()).replace("%issuer%", warning.getIssuerName()), messages.prefixWarnings);
            }

            for (String message : messages.warningsListEnd) {
                this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", playerName).replace("%warnings%", Integer.toString(user.getReports().size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
            }
        } else message.send(sender, messages.playerOffline, messages.prefixWarnings);
    }

    private void clearWarnings(CommandSender sender, String playerName) {
        IUser user = userManager.getOnOrOfflineUser(playerName);

        if (user != null) {
            WarnService.getInstance().clearWarnings(user);
            message.send(sender, messages.warningsCleared.replace("%target%", playerName), messages.prefixWarnings);
        } else message.send(sender, messages.playerOffline, messages.prefixWarnings);
    }

    private void sendWarning(CommandSender sender, String option, String reason) {
        StaffPlus.get().infractionCoordinator.sendWarning(sender, option, reason);
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7[player] [reason]", messages.prefixReports);
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}