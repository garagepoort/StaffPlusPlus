package net.shortninja.staffplus.core.domain.staff.reporting.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.FindReportsGui;
import net.shortninja.staffplusplus.reports.ReportFilters.ReportFiltersBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "reports-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class FindReportsCmd extends AbstractCmd {

    private final ReportFiltersMapper reportFiltersMapper;

    public FindReportsCmd(Messages messages, Options options, ReportFiltersMapper reportFiltersMapper, CommandService commandService) {
        super(options.commandFindReports, messages, options, commandService);
        this.reportFiltersMapper = reportFiltersMapper;
        setDescription("Find reports.");
        setUsage("[filters...]");
        setPermission(options.manageReportConfiguration.getPermissionView());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        ReportFiltersBuilder reportFiltersBuilder = new ReportFiltersBuilder();

        Arrays.stream(args).forEach(a -> {
            String[] split = a.split("=");
            if (split.length != 2) {
                throw new BusinessException("&CInvalid report filter [" + a + "]");
            }
            reportFiltersMapper.map(split[0], split[1], reportFiltersBuilder);
        });

        Player staff = (Player) sender;
        new FindReportsGui(staff, reportFiltersBuilder.build(), 0).show(staff);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return reportFiltersMapper.getFilterKeys().stream()
            .map(k -> k+="=")
            .collect(Collectors.toList());
    }
}