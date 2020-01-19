package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class AlertsCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;

    public AlertsCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (args.length >= 3 && permission.isOp(sender)) {
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            String option = args[2];

            if (targetPlayer != null) {
                handleAlertsArgument(sender, args[0], targetPlayer, false, option);
            } else message.send(sender, messages.playerOffline, messages.prefixGeneral);
        } else if (args.length == 2 && permission.isOp(sender)) {
            Player targetPlayer = Bukkit.getPlayer(args[1]);

            if (targetPlayer != null) {
                handleAlertsArgument(sender, args[0], targetPlayer, false, "");
            } else message.send(sender, messages.playerOffline, messages.prefixGeneral);
        } else if (args.length == 1 && permission.isOp(sender)) {
            if ((sender instanceof Player)) {
                handleAlertsArgument(sender, args[0], (Player) sender, true, "");
            } else message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
        } else sendHelp(sender);

        return true;
    }

    private void handleAlertsArgument(CommandSender sender, String argument, Player player, boolean shouldCheckPermission, String option) {
        if (argument.equals("namechange")) {
            argument = "name_change";
        }

        argument = argument.substring(0, 1).toUpperCase() + argument.substring(1);

        boolean wasChanged = false;
        boolean isValid = JavaUtils.isValidEnum(AlertType.class, argument.toUpperCase());
        AlertType alertType = AlertType.NAME_CHANGE;
        IUser user = userManager.get(player.getUniqueId());

        if (!isValid) {
            sendHelp(sender);
            return;
        } else if (user == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        } else alertType = AlertType.valueOf(argument.toUpperCase());

        boolean isEnabled = option.isEmpty() ? !user.shouldNotify(alertType) : (option.equalsIgnoreCase("enable") ? true : false);

        switch (alertType) {
            case NAME_CHANGE:
                if (permission.has(player, options.permissionNameChange) || !shouldCheckPermission) {
                    user.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case MENTION:
                if (permission.has(player, options.permissionMention) || !shouldCheckPermission) {
                    user.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case XRAY:
                if (permission.has(player, options.permissionXray) || !shouldCheckPermission) {
                    user.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
        }

        if (wasChanged && shouldCheckPermission) {
            message.send(player, messages.alertChanged.replace("%alerttype%", argument.replace("_", " ")).replace("%status%", isEnabled ? "enabled" : "disabled"), messages.prefixGeneral);
        }
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}