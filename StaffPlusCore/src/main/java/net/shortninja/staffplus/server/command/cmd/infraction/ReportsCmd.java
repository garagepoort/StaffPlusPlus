package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;

public class ReportsCmd extends BukkitCommand {
    private PermissionHandler permission = IocContainer.getPermissionHandler();
    private MessageCoordinator message = IocContainer.getMessage();
    private Options options = IocContainer.getOptions();
    private Messages messages = IocContainer.getMessages();
    private ReportService reportService = IocContainer.getReportService();

    public ReportsCmd(String name) {
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

            sendHelp(sender);
            return true;
        });
    }

    private void listReports(CommandSender sender, String playerName) {
        List<Report> reports = reportService.getReports(playerName, 0, 40);

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
        reportService.clearReports(playerName);
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
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1 && (!args[0].equals("get") && !args[0].equals("clear"))) {
            suggestions.add("get");
            suggestions.add("clear");
            return suggestions;
        }

        return Collections.emptyList();
    }
}