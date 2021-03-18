package net.shortninja.staffplus.staff.infractions;

import java.util.*;

public class InfractionInfo {

    private InfractionType infractionType;
    private Map<UUID, Integer> counts;
    private Map<UUID, List<String>> additionalInfo = new HashMap<>();

    public InfractionInfo(InfractionType infractionType, Map<UUID, Integer> counts) {
        this.infractionType = infractionType;
        this.counts = counts;
    }

    public InfractionInfo(InfractionType infractionType, Map<UUID, Integer> counts, Map<UUID, List<String>> additionalInfo) {
        this.infractionType = infractionType;
        this.counts = counts;
        this.additionalInfo = additionalInfo;
    }

    public Map<UUID, List<String>> getAdditionalInfo() {
        return additionalInfo;
    }

    public InfractionType getInfractionType() {
        return infractionType;
    }

    public Map<UUID, Integer> getCounts() {
        return counts;
    }
}
