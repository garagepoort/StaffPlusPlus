package net.shortninja.staffplus.domain.staff.reporting.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.domain.staff.reporting.gui.ReportTypeSelectGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportPlayerCmd extends AbstractCmd {
    private final ReportService reportService = IocContainer.getReportService();

    public ReportPlayerCmd(String name) {
        super(name, IocContainer.getOptions().permissionReport);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }
        String reason = JavaUtils.compileWords(args, 1);

        if(options.reportConfiguration.getReportTypeConfigurations().isEmpty()) {
            reportService.sendReport((Player) sender, player, reason);
        } else {
            new ReportTypeSelectGui((Player) sender, reason, player).show((Player) sender);
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return false;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
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