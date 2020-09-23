package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.CommandUtil;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.staff.warn.WarnService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WarnsCmd extends BukkitCommand {
    private PermissionHandler permission = IocContainer.getPermissionHandler();
    private MessageCoordinator message = IocContainer.getMessage();
    private Options options = IocContainer.getOptions();
    private Messages messages = IocContainer.getMessages();
    private UserManager userManager = IocContainer.getUserManager();
    private WarnService warnService = IocContainer.getWarnService();

    public WarnsCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return CommandUtil.executeCommand(sender, true, () -> {
            if (!permission.has(sender, options.permissionWarn)) {
                throw new NoPermissionException();
            }

            if (args.length == 2) {
                String argument = args[0];
                String playerName = args[1];

                if (argument.equalsIgnoreCase("get")) {
                    listWarnings(sender, playerName);
                } else if (argument.equalsIgnoreCase("clear")) {
                    clearWarnings(sender, playerName);
                } else sendHelp(sender);
            } else sendHelp(sender);

            return true;
        });
    }

    private void listWarnings(CommandSender sender, String playerName) {
        IUser user = userManager.getOnOrOfflineUser(playerName);

        if (user != null) {
            List<Warning> warnings = warnService.getWarnings(user.getUuid());

            for (String message : messages.warningsListStart) {
                this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", playerName).replace("%warnings%", Integer.toString(warnings.size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
            }

            for (int i = 0; i < warnings.size(); i++) {
                IWarning warning = warnings.get(i);

                message.send(sender, messages.warningsListEntry.replace("%count%", Integer.toString(i + 1)).replace("%reason%", warning.getReason()).replace("%issuer%", warning.getIssuerName()) + " &b" + warning.getSeverity() + ": &b" + warning.getScore(), messages.prefixWarnings);
            }

            for (String message : messages.warningsListEnd) {
                this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", playerName).replace("%warnings%", Integer.toString(warnings.size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
            }
        } else message.send(sender, messages.playerOffline, messages.prefixWarnings);
    }

    private void clearWarnings(CommandSender sender, String playerName) {
        IUser user = userManager.getOnOrOfflineUser(playerName);

        if (user != null) {
            warnService.clearWarnings(sender, user);
            message.send(sender, messages.warningsCleared.replace("%target%", playerName), messages.prefixWarnings);
        } else {
            message.send(sender, messages.playerOffline, messages.prefixWarnings);
        }
    }


    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("get");
            suggestions.add("clear");
            return suggestions;
        }

        if (args.length >= 1) {
            suggestions.addAll(onlinePLayers);
            suggestions.addAll(offlinePlayers);
            return suggestions;
        }

        return super.tabComplete(sender, alias, args);
    }
}