package net.shortninja.staffplus.core.domain.staff.reporting.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
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
    private final GuiActionService guiActionService;

    public FindReportsCmd(Messages messages,
                          ReportFiltersMapper reportFiltersMapper,
                          CommandService commandService,
                          PermissionHandler permissionHandler, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.reportFiltersMapper = reportFiltersMapper;
        this.guiActionService = guiActionService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        validateIsPlayer(sender);

        GuiActionBuilder guiActionBuilder = new GuiActionBuilder();
        guiActionBuilder.action("manage-reports/view/find-reports");

        Arrays.stream(args).forEach(a -> {
            String[] split = a.split("=");
            if (split.length != 2) {
                throw new BusinessException("&CInvalid report filter [" + a + "]");
            }
            guiActionBuilder.param(split[0], split[1]);
        });

        Player staff = (Player) sender;
        guiActionService.executeAction(staff, guiActionBuilder.build());
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
            .map(k -> k += "=")
            .collect(Collectors.toList());
    }
}