package net.shortninja.staffplus.core.domain.staff.investigate.database;

import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestigationsRepository {

    int addInvestigation(Investigation investigation);

    void updateInvestigation(Investigation investigationId);

    Optional<Investigation> getOpenedInvestigationForInvestigator(UUID investigatorUuid);

    Optional<Investigation> getOpenedInvestigationForInvestigated(UUID investigatedUuid);

    List<Investigation> getInvestigationsForInvestigator(UUID playerUUID);

    List<Investigation> getInvestigationsForInvestigated(UUID playerUUID);

}
