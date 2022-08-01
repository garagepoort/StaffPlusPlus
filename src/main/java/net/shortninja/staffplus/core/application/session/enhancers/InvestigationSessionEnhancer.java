package net.shortninja.staffplus.core.application.session.enhancers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.database.investigation.InvestigationsRepository;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.util.Collections;
import java.util.List;

@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocMultiProvider(SessionEnhancer.class)
public class InvestigationSessionEnhancer implements SessionEnhancer {
    private final InvestigationsRepository investigationsRepository;
    private final BukkitUtils bukkitUtils;

    public InvestigationSessionEnhancer(InvestigationsRepository investigationsRepository, BukkitUtils bukkitUtils) {
        this.investigationsRepository = investigationsRepository;
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        bukkitUtils.runTaskAsync(() -> {
            List<Investigation> investigation = investigationsRepository.findAllInvestigationForInvestigated(playerSession.getUuid(), Collections.singletonList(InvestigationStatus.OPEN));
            playerSession.setUnderInvestigation(!investigation.isEmpty());
        });
    }
}
