package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.database.InvestigationsRepository;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class InvestigationService {

    private final InvestigationsRepository investigationsRepository;
    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;


    public InvestigationService(InvestigationsRepository investigationsRepository, Options options, Messages messages, SessionManagerImpl sessionManager) {
        this.investigationsRepository = investigationsRepository;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    public void startInvestigation(Player investigator, SppPlayer investigated) {
        investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).ifPresent(investigation -> {
            throw new BusinessException("&CCan't start an investigation, you currently have an investigation running", messages.prefixInvestigations);
        });
        investigationsRepository.getOpenedInvestigationForInvestigated(investigated.getId()).ifPresent(investigation -> {
            throw new BusinessException("&CCan't start an investigation. This player is already being investigated.", messages.prefixInvestigations);
        });

        Investigation investigation = new Investigation(investigator.getName(), investigator.getUniqueId(), investigated.getUsername(), investigated.getId(), options.serverName);
        int id = investigationsRepository.addInvestigation(investigation);
        investigation.setId(id);
        sessionManager.get(investigated.getId()).setUnderInvestigation(true);
        sendEvent(new InvestigationStartedEvent(investigation));
    }

    public void concludeInvestigation(Player investigator) {
        Investigation investigation = investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).orElseThrow(() -> new BusinessException("&CYou currently have no investigation running", messages.prefixInvestigations));
        investigation.setConclusionDate(System.currentTimeMillis());
        investigation.setStatus(InvestigationStatus.CONCLUDED);

        investigationsRepository.updateInvestigation(investigation);
        sessionManager.get(investigation.getInvestigatedUuid()).setUnderInvestigation(false);
        sendEvent(new InvestigationConcludedEvent(investigation));
    }

}
