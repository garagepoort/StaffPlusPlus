package net.shortninja.staffplus.staff.staffchat;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.shortninja.staffplus.IocContainer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeStaffChatListener implements PluginMessageListener {

    public static final String BUNGEE_CORD = "BungeeCord";

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(BUNGEE_CORD)) {
            return;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subchannel = in.readUTF();
            if (subchannel.equals("StaffPlusPlusChat")) {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                String staffChatMessage = null;
                staffChatMessage = msgin.readUTF();
                IocContainer.getStaffChatService().handleBungeeMessage(staffChatMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
