package net.shortninja.staffplus.staff.infractions;

import java.util.Map;
import java.util.UUID;

public class InfractionCount {

    private InfractionType infractionType;

    private Map<UUID, Integer> counts;


    public InfractionCount(InfractionType infractionType, Map<UUID, Integer> counts) {
        this.infractionType = infractionType;
        this.counts = counts;
    }

    public InfractionType getInfractionType() {
        return infractionType;
    }

    public Map<UUID, Integer> getCounts() {
        return counts;
    }
}
