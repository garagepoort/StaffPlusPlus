package net.shortninja.staffplus.common.bungee;

import com.google.common.io.ByteArrayDataOutput;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import static com.google.common.io.ByteStreams.newDataOutput;
import static net.shortninja.staffplus.common.Constants.BUNGEE_CORD_CHANNEL;

public class BungeeClient {

    public void sendAll(CommandSender sender, BungeeAction action, BungeeContext context, String message) {
        send(sender, action, "ALL", context, message);
    }

    public void send(CommandSender sender, BungeeAction action, String server, BungeeContext context, String message) {
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
            try {
                ByteArrayDataOutput out = newDataOutput();
                out.writeUTF(action.getActionString());
                out.writeUTF(server);
                out.writeUTF(context.getContextString());
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeUTF(message);

                StaffPlus.get().getLogger().info("Sending bungee message on context [" + context + "]");
                StaffPlus.get().getLogger().info("Message content [" + message + "]");

                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());

                player.sendPluginMessage(StaffPlus.get(), BUNGEE_CORD_CHANNEL, out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
