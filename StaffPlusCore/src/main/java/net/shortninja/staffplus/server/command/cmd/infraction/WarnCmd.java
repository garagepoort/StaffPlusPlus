package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.config.warning.WarningSeverityConfiguration;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.staff.warn.WarnService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;

public class WarnCmd extends BukkitCommand {
    private PermissionHandler permission = IocContainer.getPermissionHandler();
    private Options options = IocContainer.getOptions();
    private Messages messages = IocContainer.getMessages();
    private WarnService warnService = IocContainer.getWarnService();

    public WarnCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {
            if (!permission.has(sender, options.permissionWarn)) {
                throw new NoPermissionException(messages.prefixWarnings);
            }
            List<WarningSeverityConfiguration> severityLevels = IocContainer.getOptions().warningConfiguration.getSeverityLevels();
            if (severityLevels.isEmpty()) {
                if (args.length < 2) {
                    throw new BusinessException(messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixWarnings);
                }
                String playerName = args[0];
                String reason = JavaUtils.compileWords(args, 1);

                warnService.sendWarning(sender, playerName, reason);
                return true;
            }

            //Handle warning with severity
            if (args.length < 3) {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixWarnings);
            }
            String severityLevel = args[0];
            String playerName = args[1];
            String reason = JavaUtils.compileWords(args, 2);

            warnService.sendWarning(sender, playerName, reason, severityLevel);
            return true;
        });
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();
        List<WarningSeverityConfiguration> severityLevels = IocContainer.getOptions().warningConfiguration.getSeverityLevels();
        if (args.length == 1) {
            if (severityLevels.isEmpty()) {
                suggestions.addAll(onlinePLayers);
                suggestions.addAll(offlinePlayers);
            } else {
                List<String> severityNames = severityLevels.stream().map(WarningSeverityConfiguration::getName).collect(Collectors.toList());
                suggestions.addAll(severityNames);
            }
            return suggestions.stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && !severityLevels.isEmpty()) {
            suggestions.addAll(onlinePLayers);
            suggestions.addAll(offlinePlayers);
            return suggestions.stream()
                    .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}