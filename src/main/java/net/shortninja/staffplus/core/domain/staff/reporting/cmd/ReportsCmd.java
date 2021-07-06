package net.shortninja.staffplus.core.domain.staff.reporting.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplusplus.reports.IReport;
import org.bukkit.command.CommandSender;

import java.util.*;

@IocBean(conditionalOnProperty = "reports-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ReportsCmd extends AbstractCmd {
    private final PermissionHandler permissionHandler;
    private final ReportService reportService;
    private final ManageReportService manageReportService;

    public ReportsCmd(PermissionHandler permissionHandler,
                      Messages messages,
                      Options options,
                      ReportService reportService,
                      ManageReportService manageReportService,
                      CommandService commandService) {

        super(options.commandReports, messages, options, commandService);
        this.permissionHandler = permissionHandler;
        this.reportService = reportService;
        this.manageReportService = manageReportService;
        setDescription("Manage Reports.");
        setUsage("[get|clear] [player]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String argument = args[0];

        if (argument.equalsIgnoreCase("get")) {
            if (!permissionHandler.has(sender, options.manageReportConfiguration.getPermissionView())) {
                throw new NoPermissionException();
            }
            listReports(sender, player);
            return true;
        }
        if (argument.equalsIgnoreCase("clear")) {
            if (!permissionHandler.has(sender, options.manageReportConfiguration.getPermissionDelete())) {
                throw new NoPermissionException();
            }
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
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getUsername()).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports);
        }

        for (int i = 0; i < reports.size(); i++) {
            IReport report = reports.get(i);
            messages.send(sender, messages.reportsListEntry
                .replace("%count%", Integer.toString(i + 1))
                .replace("%reason%", report.getReason())
                .replace("%reporter%", report.getReporterName()), messages.prefixReports);
        }

        messages.reportsListEnd
            .forEach(message -> this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getUsername()).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports));
    }

    private void clearReports(CommandSender sender, SppPlayer player) {
        manageReportService.clearReports(player);
        messages.send(sender, messages.reportsCleared.replace("%culprit%", player.getUsername()), messages.prefixReports);
    }

    private void sendHelp(CommandSender sender) {
        messages.send(sender, "&7" + messages.LONG_LINE, "");
        messages.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixReports);
        messages.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        messages.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
        messages.send(sender, "&7" + messages.LONG_LINE, "");
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