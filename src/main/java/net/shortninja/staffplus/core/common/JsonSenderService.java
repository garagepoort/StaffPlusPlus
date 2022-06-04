package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.entity.Player;

import java.util.Arrays;

@IocBean
public class JsonSenderService {

    public void send(JSONMessage jsonMessage, Player... players) {
        jsonMessage.send(players);
    }

    public void send(JSONMessage jsonMessage, String permission, Player... players) {
        Player[] collect = Arrays.stream(players).filter(p -> p.hasPermission(permission)).toArray(Player[]::new);
        jsonMessage.send(collect);
    }
}
