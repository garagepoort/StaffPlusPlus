package net.shortninja.staffplus.server.command.cmd.infraction;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;

public class ReportCmd extends BukkitCommand {
    public ReportCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {
            return true;
        });
    }
}