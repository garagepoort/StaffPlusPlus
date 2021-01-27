package net.shortninja.staffplus.session.bungee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static net.shortninja.staffplus.common.Constants.BUNGEE_CORD_CHANNEL;

public class BungeeSessionListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(BUNGEE_CORD_CHANNEL)) {
            return;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subchannel = in.readUTF();
            if (subchannel.equals("StaffPlusPlusSession")) {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                String sessionDtoJson = msgin.readUTF();
                StaffPlus.get().getLogger().info("Retrieved session update: " + sessionDtoJson);
                SessionBungeeDto sessionBungeeDto = new ObjectMapper().readValue(sessionDtoJson, SessionBungeeDto.class);
                IocContainer.getSessionManager().handleSessionSync(sessionBungeeDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
