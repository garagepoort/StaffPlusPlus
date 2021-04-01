package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.database.InvestigationsRepository;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionEnhancer;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.util.Collections;
import java.util.Optional;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class InvestigationSessionEnhancer implements SessionEnhancer {

    private final InvestigationsRepository investigationsRepository;

    public InvestigationSessionEnhancer(InvestigationsRepository investigationsRepository) {
        this.investigationsRepository = investigationsRepository;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        Optional<Investigation> investigation = investigationsRepository.getInvestigationForInvestigated(playerSession.getUuid(), Collections.singletonList(InvestigationStatus.OPEN));
        playerSession.setUnderInvestigation(investigation.isPresent());
    }
}
