package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.ui.ArgumentProcessor;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;

public class ClearInvCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();

    public ClearInvCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {

            if (args.length > 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (permission.has(player, options.permissionClearInv))
                        if (Bukkit.getServer().getPlayer(args[0]) != null) {
                            List<String> arguments = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));

                            Player target = Bukkit.getServer().getPlayer(args[0]);
                            JavaUtils.clearInventory(target);
                            sender.sendMessage(target.getName() + "'s inventory has been cleared");
                            argumentProcessor.parseArguments(sender, args[0], arguments);
                            return true;
                        }
                }
            } else {
                sendHelp(sender);
            }
            return true;
        });
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}
