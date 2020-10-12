package net.shortninja.staffplus.staff.warn.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.WarnService;
import net.shortninja.staffplus.staff.warn.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WarnCmd extends AbstractCmd {
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final WarnService warnService = IocContainer.getWarnService();

    public WarnCmd(String name) {
        super(name, IocContainer.getOptions().permissionWarn);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        List<WarningSeverityConfiguration> severityLevels = options.warningConfiguration.getSeverityLevels();
        if (severityLevels.isEmpty()) {
            String reason = JavaUtils.compileWords(args, 1);
            warnService.sendWarning(sender, player, reason);
            return true;
        }

        //Handle warning with severity
        String severityLevel = args[0];
        String reason = JavaUtils.compileWords(args, 2);
        warnService.sendWarning(sender, player, reason, severityLevel);
        return true;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permission.has(player, options.permissionWarnBypass);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        List<WarningSeverityConfiguration> severityLevels = options.warningConfiguration.getSeverityLevels();
        if (severityLevels.isEmpty()) {
            return 2;
        }
        return 3;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        List<WarningSeverityConfiguration> severityLevels = options.warningConfiguration.getSeverityLevels();
        if (severityLevels.isEmpty()) {
            return Optional.of(args[0]);
        }
        return Optional.of(args[1]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<WarningSeverityConfiguration> severityLevels = options.warningConfiguration.getSeverityLevels();
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            if (severityLevels.isEmpty()) {
                suggestions.addAll(playerManager.getAllPlayerNames());
            } else {
                List<String> severityNames = severityLevels.stream().map(WarningSeverityConfiguration::getName).collect(Collectors.toList());
                suggestions.addAll(severityNames);
            }
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2 && !severityLevels.isEmpty()) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}