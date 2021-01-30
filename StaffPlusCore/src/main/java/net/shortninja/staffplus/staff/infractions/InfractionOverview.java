package net.shortninja.staffplus.staff.infractions;

import net.shortninja.staffplus.player.SppPlayer;

import java.util.HashMap;
import java.util.Map;

public class InfractionOverview {
    private SppPlayer sppPlayer;
    private Map<String, Integer> infractions = new HashMap<>();


    public SppPlayer getSppPlayer() {
        return sppPlayer;
    }

    public Map<String, Integer> getInfractions() {
        return infractions;
    }
    public void setSppPlayer(SppPlayer sppPlayer) {
        this.sppPlayer = sppPlayer;
    }

    public void setInfractions(Map<String, Integer> infractions) {
        this.infractions = infractions;
    }

    public int getTotal() {
        return infractions.values().stream().mapToInt(Integer::intValue).sum();
    }
}
