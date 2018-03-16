package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Random;

public class TpCommand extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;

    public TpCommand(String name)
    {
        super(name);
    }
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!permission.has(sender, options.permissionTp))
        {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return true;
        }

        if(args.length == 1 && permission.isOp(sender))
        {
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if(targetPlayer != null)
            {
                teleport(targetPlayer);
            }else message.send(sender, messages.playerOffline, messages.prefixGeneral);
        }else message.send(sender, messages.playerOffline, messages.prefixGeneral);

        return true;
    }

    private void teleport(Player player){
        Random  random = new Random();
        int x = random.nextInt(options.maxX - options.minX + 1) + options.minX;
        int z = random.nextInt(options.maxZ - options.minZ + 1) + options.minZ;
        int y = player.getWorld().getHighestBlockYAt(x,z);
        Location location = new Location(player.getWorld(), x+.5, y, z+.5, 0F,0F);
        player.teleport(location);

    }
}
