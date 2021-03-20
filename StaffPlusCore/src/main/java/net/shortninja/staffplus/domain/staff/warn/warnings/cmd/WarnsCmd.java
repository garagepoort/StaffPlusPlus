package net.shortninja.staffplus.domain.staff.warn.warnings.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WarnsCmd extends AbstractCmd {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final WarnService warnService = IocContainer.getWarnService();

    public WarnsCmd(String name) {
        super(name, IocContainer.getOptions().permissionWarn);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (args.length == 2) {
            String argument = args[0];
            if (argument.equalsIgnoreCase("get")) {
                listWarnings(sender, player);
            } else sendHelp(sender);
        } else sendHelp(sender);

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

    private void listWarnings(CommandSender sender, SppPlayer player) {
        List<Warning> warnings = warnService.getWarnings(player.getId(), true);

        for (String message : messages.warningsListStart) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getUsername()).replace("%warnings%", Integer.toString(warnings.size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
        }

        for (int i = 0; i < warnings.size(); i++) {
            IWarning warning = warnings.get(i);

            message.send(sender, messages.warningsListEntry.replace("%count%", Integer.toString(i + 1)).replace("%reason%", warning.getReason()).replace("%issuer%", warning.getIssuerName()) + " &b" + warning.getSeverity() + ": &b" + warning.getScore(), messages.prefixWarnings);
        }

        for (String message : messages.warningsListEnd) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getUsername()).replace("%warnings%", Integer.toString(warnings.size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
        }
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of("get")
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length >= 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return super.tabComplete(sender, alias, args);
    }
}