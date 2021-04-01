package net.shortninja.staffplus.core.domain.staff.investigate.database;

import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestigationsRepository {

    int addInvestigation(Investigation investigation);

    void updateInvestigation(Investigation investigation);

    Optional<Investigation> getOpenedInvestigationForInvestigator(UUID investigatorUuid);

    Optional<Investigation> getInvestigationForInvestigated(UUID investigatedUuid, List<InvestigationStatus> investigationStatuses);

    List<Investigation> getInvestigationsForInvestigator(UUID playerUUID);

    List<Investigation> getInvestigationsForInvestigated(UUID playerUUID);

    List<Investigation> getAllInvestigations(int offset, int amount);

    List<Investigation> getInvestigationsForInvestigated(UUID id, int offset, int amount);

    Optional<Investigation> findInvestigation(int investigationId);
}
