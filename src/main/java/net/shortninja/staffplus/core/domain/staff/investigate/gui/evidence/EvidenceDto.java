package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import net.shortninja.staffplusplus.investigate.evidence.Evidence;

public class EvidenceDto implements Evidence {
    private int id;
    private String evidenceType;
    private String description;

    public EvidenceDto(int id, String evidenceType, String description) {
        this.id = id;
        this.evidenceType = evidenceType;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getEvidenceType() {
        return evidenceType;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
