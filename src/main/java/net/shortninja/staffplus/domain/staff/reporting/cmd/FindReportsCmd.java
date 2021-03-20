package net.shortninja.staffplus.domain.staff.reporting.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.domain.staff.reporting.ReportFilters.ReportFiltersBuilder;
import net.shortninja.staffplus.domain.staff.reporting.gui.FindReportsGui;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FindReportsCmd extends AbstractCmd {

    private final PermissionHandler permissionHandler = IocContainer.getPermissionHandler();
    private final ReportFiltersMapper reportFiltersMapper = IocContainer.getReportFiltersMapper();

    public FindReportsCmd(String name) {
        super(name);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!permissionHandler.has(sender, options.manageReportConfiguration.getPermissionView())) {
            throw new NoPermissionException();
        }
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