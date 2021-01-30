package net.shortninja.staffplus.staff.infractions;

import java.util.Map;
import java.util.UUID;

public class InfractionCount {

    private String infractionType;

    private Map<UUID, Integer> counts;


    public InfractionCount(String infractionType, Map<UUID, Integer> counts) {
        this.infractionType = infractionType;
        this.counts = counts;
    }

    public String getInfractionType() {
        return infractionType;
    }

    public Map<UUID, Integer> getCounts() {
        return counts;
    }
}
