package net.shortninja.staffplus.core.domain.staff.investigate;

import net.shortninja.staffplusplus.investigate.EvidenceType;

public interface Evidence {

    int getId();

    EvidenceType getEvidenceType();

    String getDescription();
}
