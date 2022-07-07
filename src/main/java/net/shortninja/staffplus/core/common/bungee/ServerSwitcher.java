package net.shortninja.staffplus.core.common.bungee;

import com.google.common.io.ByteArrayDataOutput;
import net.shortninja.staffplus.core.StaffPlusPlus;
import org.bukkit.entity.Player;

import static com.google.common.io.ByteStreams.newDataOutput;

public class ServerSwitcher {

    public static void switchServer(Player player, String serverName) {
        ByteArrayDataOutput out = newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(StaffPlusPlus.get(), "BungeeCord", out.toByteArray());
    }
}
