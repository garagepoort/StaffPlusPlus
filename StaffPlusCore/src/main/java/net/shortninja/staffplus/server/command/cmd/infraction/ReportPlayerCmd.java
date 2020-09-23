package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
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

public class ReportPlayerCmd extends BukkitCommand {
    private MessageCoordinator message = IocContainer.getMessage();
    private Messages messages = IocContainer.getMessages();
    private ReportService reportService = IocContainer.getReportService();

    public ReportPlayerCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, false, () -> {
            if (args.length < 2) {
                sendHelp(sender);
                return true;
            }

            String playerName = args[0];
            String reason = JavaUtils.compileWords(args, 1);
            reportService.sendReport(sender, playerName, reason);
            return true;
        });
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixReports);
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.addAll(onlinePLayers);
            suggestions.addAll(offlinePlayers);
            return suggestions.stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}