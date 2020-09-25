package net.shortninja.staffplus.reporting.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReportsCmd extends AbstractCmd {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final ReportService reportService = IocContainer.getReportService();

    public ReportsCmd(String name) {
        super(name, IocContainer.getOptions().permissionReport);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String argument = args[0];

        if (argument.equalsIgnoreCase("get")) {
            listReports(sender, player);
            return true;
        }
        if (argument.equalsIgnoreCase("clear")) {
            clearReports(sender, player);
            return true;
        }

        sendHelp(sender);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[1]);
    }

    private void listReports(CommandSender sender, SppPlayer player) {
        List<Report> reports = reportService.getReports(player, 0, 40);

        for (String message : messages.reportsListStart) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getUsername()).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports);
        }

        for (int i = 0; i < reports.size(); i++) {
            IReport report = reports.get(i);
            message.send(sender, messages.reportsListEntry
                .replace("%count%", Integer.toString(i + 1))
                .replace("%reason%", report.getReason())
                .replace("%reporter%", report.getReporterName()), messages.prefixReports);
        }

        messages.reportsListEnd
            .forEach(message -> this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getUsername()).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports));
    }

    private void clearReports(CommandSender sender, SppPlayer player) {
        reportService.clearReports(player);
        message.send(sender, messages.reportsCleared.replace("%target%", player.getUsername()), messages.prefixReports);
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