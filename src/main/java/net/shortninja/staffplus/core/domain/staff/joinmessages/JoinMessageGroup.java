package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.configuration.ConfigProperty;

public class JoinMessageGroup {

    @ConfigProperty("permission")
    private String permission;
    @ConfigProperty("message")
    private String message;
    @ConfigProperty("weight")
    private int weight;

    public String getPermission() {
        return permission;
    }

    public String getMessage() {
        return message;
    }

    public int getWeight() {
        return weight;
    }
}
