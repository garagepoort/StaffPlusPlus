package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;

import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.database.InvestigationsRepository;
import org.bukkit.entity.Player;

@IocBean
public class InvestigationService {

    private final InvestigationsRepository investigationsRepository;
    private final Options options;
    private final Messages messages;


    public InvestigationService(InvestigationsRepository investigationsRepository, Options options, Messages messages) {
        this.investigationsRepository = investigationsRepository;
        this.options = options;
        this.messages = messages;
    }

    public void startInvestigation(Player investigator, SppPlayer investigated) {
        investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).ifPresent(investigation -> {
            throw new BusinessException("Can't start an investigation, you currently have an investigation running");
        });
        investigationsRepository.getOpenedInvestigationForInvestigated(investigated.getId()).ifPresent(investigation -> {
            throw new BusinessException("Can't start an investigation. This player is already being investigated.");
        });

        Investigation investigation = new Investigation(investigator.getName(), investigator.getUniqueId(), investigated.getUsername(), investigated.getId(), options.serverName);
        investigationsRepository.addInvestigation(investigation);
    }

}
