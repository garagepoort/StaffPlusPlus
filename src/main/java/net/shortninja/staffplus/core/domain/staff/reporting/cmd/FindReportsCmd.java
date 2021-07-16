package net.shortninja.staffplus.core.domain.staff.reporting.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.FindReportsGui;
import net.shortninja.staffplusplus.reports.ReportFilters.ReportFiltersBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:reports.manage.gui-find-reports",
    permissions = "permissions:reports.manage.view",
    description = "Find reports.",
    usage = "[filters...]"
)
@IocBean(conditionalOnProperty = "reports-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class FindReportsCmd extends AbstractCmd {

    private final ReportFiltersMapper reportFiltersMapper;

    public FindReportsCmd(Messages messages,
                          Options options,
                          ReportFiltersMapper reportFiltersMapper,
                          CommandService commandService,
                          PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.reportFiltersMapper = reportFiltersMapper;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
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