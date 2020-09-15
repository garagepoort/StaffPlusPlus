package net.shortninja.staffplus.player;

import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NodeUser {
    private IUser user;
    private String prefix;

    public NodeUser(IUser user) {
        this.user = user;
        this.prefix = user.getUuid() + ".";
    }

    public String prefix() {
        return prefix;
    }

    public String name() {
        return user.getName();
    }

//    public String password() {
//        return securityHandler.getPassword(user.getUuid());
//    }

    public short glassColor() {
        return user.getGlassColor();
    }

    public List<String> playerNotes() {
        return new ArrayList<>(user.getPlayerNotes());
    }

    public List<String> alertOptions() {
        List<String> alertsList = new ArrayList<>();

        for (AlertType alertType : AlertType.values()) {
            alertsList.add(alertType.name() + ";" + user.shouldNotify(alertType));
        }

        return alertsList;
    }

    public UUID getUUID() {
        return user.getUuid();
    }
}