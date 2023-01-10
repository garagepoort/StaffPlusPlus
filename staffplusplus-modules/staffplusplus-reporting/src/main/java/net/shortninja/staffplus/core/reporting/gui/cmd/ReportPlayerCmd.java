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
import net.shortninja.staffplus.core.domain.player.PlayerManager;
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
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.application.SppInteractorBuilder.fromSender;
import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;

@Command(
    command = "commands:reportPlayer",
    permissions = "permissions:report",
    description = "Sends a report with the given player and reason.",
    usage = "[player] [reason]",
    playerRetrievalStrategy = BOTH
)
@IocBean(conditionalOnProperty = "reports-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ReportPlayerCmd extends AbstractCmd {
    private static final CulpritFilterPredicate culpritFilterPredicate = new CulpritFilterPredicate(true);
    private final List<ReportTypeConfiguration> reportTypeConfigurations;
    private final List<ReportReasonConfiguration> reportReasonConfigurations;

    private final ReportService reportService;
    private final PlayerManager playerManager;
    private final BukkitUtils bukkitUtils;
    private final ReportConfiguration reportConfiguration;
    private final GuiActionService guiActionService;

    public ReportPlayerCmd(Messages messages,
                           ReportService reportService,
                           PlayerManager playerManager,
                           CommandService commandService,
                           PermissionHandler permissionHandler,
                           BukkitUtils bukkitUtils,
                           ReportConfiguration reportConfiguration, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.reportService = reportService;
        this.playerManager = playerManager;
        this.reportConfiguration = reportConfiguration;
        this.guiActionService = guiActionService;
        reportTypeConfigurations = this.reportConfiguration.getReportTypeConfigurations(culpritFilterPredicate);
        reportReasonConfigurations = this.reportConfiguration.getReportReasonConfigurations(culpritFilterPredicate);
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String reason = reportConfiguration.isFixedReasonCulprit() ? null : JavaUtils.compileWords(args, 1);

        if (reportTypeConfigurations.isEmpty() && reportReasonConfigurations.isEmpty()) {
            bukkitUtils.runTaskAsync(sender, () -> reportService.sendReport(fromSender(sender), player, reason));
            return true;
        }

        if (!reportTypeConfigurations.isEmpty()) {
            guiActionService.executeAction((Player) sender, GuiActionBuilder.builder()
                .action("reports/view/type-select")
                .param("reason", reason)
                .param("culprit", player.getUsername())
                .build());
            return true;
        }
        guiActionService.executeAction((Player) sender, GuiActionBuilder.builder()
            .action("reports/view/reason-select")
            .param("culprit", player.getUsername())
            .build());
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (reportReasonConfigurations.isEmpty()) {
            return 2;
        }
        return 1;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return false;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}