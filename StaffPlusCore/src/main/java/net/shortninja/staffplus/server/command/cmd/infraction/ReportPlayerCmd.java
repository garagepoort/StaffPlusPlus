package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportPlayerService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;

public class ReportPlayerCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = IocContainer.getMessages();
    private ReportPlayerService reportPlayerService = IocContainer.getReportPlayerService();

    public ReportPlayerCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {
            if (!permission.has(sender, options.permissionReport)) {
                throw new NoPermissionException(messages.prefixReports);
            }
            if (args.length < 2) {
                sendHelp(sender);
                return true;
            }

            String argument = args[0];
            String playerName = args[1];
            boolean hasPermission = permission.has(sender, options.permissionReport);

            if (argument.equalsIgnoreCase("get") && hasPermission) {
                listReports(sender, playerName);
                return true;
            }
            if (argument.equalsIgnoreCase("clear") && hasPermission) {
                clearReports(sender, playerName);
                return true;
            }

            playerName = args[0];
            String reason = JavaUtils.compileWords(args, 1);
            reportPlayerService.sendReport(sender, playerName, reason);
            return true;
        });
    }

    private void listReports(CommandSender sender, String playerName) {
        List<Report> reports = reportPlayerService.getReports(playerName);

        for (String message : messages.reportsListStart) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", playerName).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports);
        }

        for (int i = 0; i < reports.size(); i++) {
            IReport report = reports.get(i);
            message.send(sender, messages.reportsListEntry
                    .replace("%count%", Integer.toString(i + 1))
                    .replace("%reason%", report.getReason())
                    .replace("%reporter%", report.getReporterName()), messages.prefixReports);
        }

        messages.reportsListEnd
                .forEach(message -> this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", playerName).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports));
    }

    private void clearReports(CommandSender sender, String playerName) {
        reportPlayerService.clearReports(playerName);
        message.send(sender, messages.reportsCleared.replace("%target%", playerName), messages.prefixReports);
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixReports);
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        boolean hasPermission = permission.has(sender, options.permissionReport);

        List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1 && (!args[0].equals("get") && !args[0].equals("clear"))) {
            if (hasPermission) {
                suggestions.add("get");
                suggestions.add("clear");
            }

            suggestions.addAll(onlinePLayers);
            suggestions.addAll(offlinePlayers);
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