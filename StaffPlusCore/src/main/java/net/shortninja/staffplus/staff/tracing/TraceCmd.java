package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.common.CommandUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class TraceCmd extends BukkitCommand {

    public TraceCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return CommandUtil.executeCommand(sender, () -> {

            return true;
        });
    }
}
