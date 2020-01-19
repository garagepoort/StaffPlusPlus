package net.shortninja.staffplus.server.command.cmd.infraction;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportCmd extends BukkitCommand {
    private static Map<UUID, Long> lastUse = new HashMap<UUID, Long>();
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;

    public ReportCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (args.length >= 2) {
            String argument = args[0];
            String option = args[1];
            boolean hasPermission = permission.has(sender, options.permissionReport);
            Player player = Bukkit.getPlayer(option);

            if (argument.equalsIgnoreCase("get") && hasPermission) {
                if (player == null) {
                    message.send(sender, messages.playerOffline, messages.prefixReports);
                } else listReports(sender, player);
            } else if (argument.equalsIgnoreCase("clear") && hasPermission) {
                if (player == null) {
                    message.send(sender, messages.playerOffline, messages.prefixReports);
                } else clearReports(sender, player);
            } else {
                long now = System.currentTimeMillis();
                long last = sender instanceof Player ? (lastUse.containsKey(((Player) sender).getUniqueId()) ? lastUse.get(((Player) sender).getUniqueId()) : 0) : 0;
                long remaining = (now - last) / 1000;

                if (remaining < options.reportsCooldown && !permission.has(sender, options.permissionReport)) {
                    message.send(sender, messages.commandOnCooldown.replace("%seconds%", Long.toString(options.reportsCooldown - remaining)), messages.prefixGeneral);
                } else {
                    sendReport(sender, args[0], JavaUtils.compileWords(args, 1));

                    if (sender instanceof Player) {
                        lastUse.put(((Player) sender).getUniqueId(), now);
                    }
                }
            }
        } else if (!permission.has(sender, options.permissionReport)) {
            message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixReports);
        } else sendHelp(sender);

        return true;
    }

    private void listReports(CommandSender sender, Player player) {
        IUser user = userManager.get(player.getUniqueId());

        if (user != null) {
            List<IReport> reports = user.getReports();

            for (String message : messages.reportsListStart) {
                this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports);
            }

            for (int i = 0; i < reports.size(); i++) {
                IReport report = reports.get(i);

                message.send(sender, messages.reportsListEntry.replace("%count%", Integer.toString(i + 1)).replace("%reason%", report.getReason()).replace("%reporter%", report.getReporterName()), messages.prefixReports);
            }

            for (String message : messages.reportsListEnd) {
                this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%reports%", Integer.toString(reports.size())), message.contains("%longline%") ? "" : messages.prefixReports);
            }
        } else message.send(sender, messages.playerOffline, messages.prefixReports);
    }

    private void clearReports(CommandSender sender, Player player) {
        IUser user = userManager.get(player.getUniqueId());

        if (user != null) {
            StaffPlus.get().infractionCoordinator.clearReports(user);
            message.send(sender, messages.reportsCleared.replace("%target%", player.getName()), messages.prefixReports);
        } else message.send(sender, messages.playerOffline, messages.prefixReports);
    }

    private void sendReport(CommandSender sender, String option, String reason) {
        String reporterName = "Console"; // The name "Console" is taken, but it is capitalized as "CONSOLE".
        UUID reporterUuid = null;
        Player reported = Bukkit.getPlayer(option);


        if (reported != null) {
            Player reporter = null;

            if (sender instanceof Player) {
                reporter = (Player) sender;
                reporterName = reporter.getName();
                reporterUuid = reporter.getUniqueId();
            } else
                reporterUuid = StaffPlus.get().consoleUUID;
            Report report = new Report(reported.getUniqueId(), reported.getName(), reason, reporterName, reporterUuid);
            StaffPlus.get().infractionCoordinator.addUnresolvedReport(report);
            StaffPlus.get().infractionCoordinator.sendReport(sender, report);
        } else message.send(sender, messages.playerOffline, messages.prefixGeneral);
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixReports);
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
        message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}