package net.shortninja.staffplus.core.domain.staff.staffchat;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.shortninja.staffplus.core.application.IocContainer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

public class BungeeStaffChatListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(BUNGEE_CORD_CHANNEL)) {
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
                IocContainer.get(StaffChatServiceImpl.class).handleBungeeMessage(staffChatMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
