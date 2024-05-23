package net.shortninja.staffplus.core.domain.staff.broadcast;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitMessageListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.shortninja.staffplus.core.common.bungee.BungeeContext;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBukkitMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class BungeeBroadcastListener implements PluginMessageListener {

    private final BroadcastService broadcastService;

    public BungeeBroadcastListener(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(BUNGEE_CORD_CHANNEL)) {
            return;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subchannel = in.readUTF();
            if (subchannel.equals(BungeeContext.BROADCAST.getContextString())) {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                String broadcastMessage = msgin.readUTF();
                broadcastService.handleBungeeBroadcast(broadcastMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
