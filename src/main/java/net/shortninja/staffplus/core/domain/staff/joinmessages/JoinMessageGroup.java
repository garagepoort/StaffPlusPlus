package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplusplus.joinmessages.IJoinMessageGroup;

public class JoinMessageGroup implements IJoinMessageGroup {

    @ConfigProperty("permission")
    private String permission;
    @ConfigProperty("message")
    private String message;
    @ConfigProperty("weight")
    private int weight;

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
