package net.shortninja.staffplus.core.reporting.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.reporting.ReportService;
import net.shortninja.staffplus.core.reporting.config.CulpritFilterPredicate;
import net.shortninja.staffplus.core.reporting.config.ReportConfiguration;
import net.shortninja.staffplus.core.reporting.config.ReportReasonConfiguration;
import net.shortninja.staffplus.core.reporting.config.ReportTypeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.application.SppInteractorBuilder.fromSender;

@Command(
    command = "commands:report",
    permissions = "permissions:report",
    description = "Sends a report without a specific player.",
    usage = "[reason]"
)
@IocBean(conditionalOnProperty = "reports-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ReportCmd extends AbstractCmd {
    private static final CulpritFilterPredicate culpritFilterPredicate = new CulpritFilterPredicate(false);
    private final ReportService reportService;
    private final List<ReportTypeConfiguration> reportTypeConfigurations;
    private final List<ReportReasonConfiguration> reportReasonConfigurations;
    private final BukkitUtils bukkitUtils;
    private final ReportConfiguration reportConfiguration;
    private final GuiActionService guiActionService;

    public ReportCmd(Messages messages,
                     ReportService reportService,
                     CommandService commandService,
                     PermissionHandler permissionHandler, BukkitUtils bukkitUtils, ReportConfiguration reportConfiguration, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.reportService = reportService;
        reportTypeConfigurations = reportConfiguration.getReportTypeConfigurations(culpritFilterPredicate);
        reportReasonConfigurations = reportConfiguration.getReportReasonConfigurations(culpritFilterPredicate);
        this.bukkitUtils = bukkitUtils;
        this.reportConfiguration = reportConfiguration;
        this.guiActionService = guiActionService;
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String reason = reportConfiguration.isFixedReason() ? null : JavaUtils.compileWords(args, 0);

        if (reportTypeConfigurations.isEmpty() && reportReasonConfigurations.isEmpty()) {
            bukkitUtils.runTaskAsync(sender, () -> reportService.sendReport(fromSender(sender), reason));
            return true;
        }

        if (!reportTypeConfigurations.isEmpty()) {
            guiActionService.executeAction((Player) sender, GuiActionBuilder.builder()
                .action("reports/view/type-select")
                .param("reason", reason)
                .build());
            return true;
        }

        guiActionService.executeAction((Player) sender, "reports/view/reason-select");
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (reportReasonConfigurations.isEmpty()) {
            return 1;
        }
        return 0;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return false;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}