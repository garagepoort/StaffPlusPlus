package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.api.JsonSenderFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JsonSender;
import org.bukkit.entity.Player;

@IocBean
public class JsonSenderService {

    private final JsonSender jsonSender;

    public JsonSenderService() {
        jsonSender = JsonSenderFactory.getSender();
    }

    public void send(JSONMessage jsonMessage, Player... players) {
        jsonSender.send(jsonMessage, players);
    }
}
