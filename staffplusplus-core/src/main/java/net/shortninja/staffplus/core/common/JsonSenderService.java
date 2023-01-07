package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.api.JsonSenderFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JsonSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@IocBean
public class JsonSenderService {

    private final JsonSender jsonSender;

    public JsonSenderService() {
        jsonSender = JsonSenderFactory.getSender();
    }

    public void send(JSONMessage jsonMessage, Player... players) {
        jsonSender.send(jsonMessage, players);
    }

    public void send(JSONMessage jsonMessage, String permission, Player... players) {
        Player[] collect = Arrays.stream(players).filter(p -> p.hasPermission(permission)).toArray(Player[]::new);
        jsonSender.send(jsonMessage, collect);
    }
}
