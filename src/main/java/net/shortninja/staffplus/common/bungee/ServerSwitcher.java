package net.shortninja.staffplus.common.bungee;

import com.google.common.io.ByteArrayDataOutput;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.google.common.io.ByteStreams.newDataOutput;

public class ServerSwitcher {

    public static void switchServer(Player player, String serverName) {
        sendBungeeMessage(player, serverName);
    }

    private static void sendBungeeMessage(CommandSender sender, String server) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            if (onlinePlayers.iterator().hasNext()) {
                player = onlinePlayers.iterator().next();
            }
        }
        if (player != null) {
            ByteArrayDataOutput out = newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(StaffPlus.get(), "BungeeCord", out.toByteArray());
        }
    }
}
