package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.database.investigation.InvestigationsRepository;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.util.Collections;
import java.util.List;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class InvestigationSessionEnhancer implements SessionEnhancer {

    private final InvestigationsRepository investigationsRepository;

    public InvestigationSessionEnhancer(InvestigationsRepository investigationsRepository) {
        this.investigationsRepository = investigationsRepository;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        List<Investigation> investigation = investigationsRepository.findAllInvestigationForInvestigated(playerSession.getUuid(), Collections.singletonList(InvestigationStatus.OPEN));
        playerSession.setUnderInvestigation(!investigation.isEmpty());
    }
}
