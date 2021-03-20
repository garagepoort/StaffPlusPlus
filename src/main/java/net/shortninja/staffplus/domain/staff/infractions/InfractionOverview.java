package net.shortninja.staffplus.domain.staff.infractions;

import net.shortninja.staffplus.domain.player.SppPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfractionOverview {
    private SppPlayer sppPlayer;
    private Map<InfractionType, Integer> infractions = new HashMap<>();
    private List<String> additionalInfo = new ArrayList<>();


    public SppPlayer getSppPlayer() {
        return sppPlayer;
    }

    public Map<InfractionType, Integer> getInfractions() {
        return infractions;
    }
    public void setSppPlayer(SppPlayer sppPlayer) {
        this.sppPlayer = sppPlayer;
    }

    public void setInfractions(Map<InfractionType, Integer> infractions) {
        this.infractions = infractions;
    }

    public int getTotal() {
        return infractions.values().stream().mapToInt(Integer::intValue).sum();
    }

    public List<String> getAdditionalInfo() {
        return additionalInfo;
    }
}
