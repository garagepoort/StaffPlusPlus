package net.shortninja.staffplus.core.domain.staff.investigate.database.evidence;

import net.shortninja.staffplus.core.domain.staff.investigate.Evidence;
import net.shortninja.staffplus.core.domain.staff.investigate.EvidenceEntity;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;

import java.util.List;
import java.util.Optional;

public interface InvestigationEvidenceRepository {

    void addEvidence(EvidenceEntity evidenceEntity);

    List<EvidenceEntity> getAllEvidence(int investigationId);

    List<EvidenceEntity> getAllEvidence(int investigationId, int offset, int amount);

    void removeEvidence(int id);

    Optional<EvidenceEntity> findLinkedEvidence(Investigation investigation, Evidence evidence);

    Optional<EvidenceEntity> find(int id);
}
