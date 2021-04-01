package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.database.InvestigationsRepository;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplusplus.investigate.InvestigationStatus.OPEN;
import static net.shortninja.staffplusplus.investigate.InvestigationStatus.PAUSED;

@IocBean
public class InvestigationService {

    private final InvestigationsRepository investigationsRepository;
    private final Options options;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;


    public InvestigationService(InvestigationsRepository investigationsRepository, Options options, Messages messages, BukkitUtils bukkitUtils) {
        this.investigationsRepository = investigationsRepository;
        this.options = options;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
    }

    public void startInvestigation(Player investigator, SppPlayer investigated) {
        bukkitUtils.runTaskAsync(investigator, () -> {
            if (!investigated.isOnline() && !options.investigationConfiguration.isAllowOfflineInvestigation()) {
                throw new BusinessException("Not allowed to investigate an offline player");
            }
            investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).ifPresent(investigation1 -> {
                throw new BusinessException("&CCan't start an investigation, you currently have an investigation running", messages.prefixInvestigations);
            });
            investigationsRepository.getInvestigationForInvestigated(investigated.getId(), Arrays.asList(OPEN, PAUSED)).ifPresent(investigation1 -> {
                throw new BusinessException("&CCan't start an investigation. This player is already being investigated.", messages.prefixInvestigations);
            });

            Investigation investigation = new Investigation(investigator.getName(), investigator.getUniqueId(), investigated.getUsername(), investigated.getId(), options.serverName);
            int id = investigationsRepository.addInvestigation(investigation);
            investigation.setId(id);
            sendEvent(new InvestigationStartedEvent(investigation));
        });
    }

    public void resumeInvestigation(Player investigator, SppPlayer investigated) {
        bukkitUtils.runTaskAsync(investigator, () -> {
            if (!investigated.isOnline() && !options.investigationConfiguration.isAllowOfflineInvestigation()) {
                throw new BusinessException("Not allowed to investigate an offline player");
            }
            investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).ifPresent(investigation1 -> {
                throw new BusinessException("&CCan't start an investigation, you currently have an investigation running", messages.prefixInvestigations);
            });
            investigationsRepository.getInvestigationForInvestigated(investigated.getId(), Collections.singletonList(OPEN)).ifPresent(investigation1 -> {
                throw new BusinessException("&CCan't start an investigation. This player is already being investigated.", messages.prefixInvestigations);
            });

            Investigation investigation = investigationsRepository.getInvestigationForInvestigated(investigated.getId(), Collections.singletonList(PAUSED))
                .orElseThrow(() -> new BusinessException("Cannot resume investigation. No paused investigation found"));
            investigation.setStatus(OPEN);
            investigation.setInvestigatorName(investigator.getName());
            investigation.setInvestigatorUuid(investigator.getUniqueId());
            investigationsRepository.updateInvestigation(investigation);
            sendEvent(new InvestigationStartedEvent(investigation));
        });
    }

    public Optional<Investigation> getPausedInvestigation(SppPlayer investigated) {
        return investigationsRepository.getInvestigationForInvestigated(investigated.getId(), Collections.singletonList(PAUSED));
    }

    public void concludeInvestigation(Player investigator) {
        bukkitUtils.runTaskAsync(investigator, () -> {
            Investigation investigation = investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).orElseThrow(() -> new BusinessException("&CYou currently have no investigation running", messages.prefixInvestigations));
            investigation.setConclusionDate(System.currentTimeMillis());
            investigation.setStatus(InvestigationStatus.CONCLUDED);

            investigationsRepository.updateInvestigation(investigation);
            sendEvent(new InvestigationConcludedEvent(investigation));
        });
    }

    public void pauseInvestigation(Player investigator) {
        bukkitUtils.runTaskAsync(investigator, () -> {
            Investigation investigation = investigationsRepository.getOpenedInvestigationForInvestigator(investigator.getUniqueId()).orElseThrow(() -> new BusinessException("&CYou currently have no investigation running", messages.prefixInvestigations));
            investigation.setStatus(InvestigationStatus.PAUSED);

            investigationsRepository.updateInvestigation(investigation);
            sendEvent(new InvestigationPausedEvent(investigation));
        });
    }

    public List<Investigation> getAllInvestigations(int offset, int amount) {
        return investigationsRepository.getAllInvestigations(offset, amount);
    }

    public List<Investigation> getInvestigationsForInvestigated(SppPlayer sppPlayer, int offset, int amount) {
        return investigationsRepository.getInvestigationsForInvestigated(sppPlayer.getId(), offset, amount);
    }

    public Investigation getInvestigation(int investigationId) {
        return investigationsRepository.findInvestigation(investigationId)
            .orElseThrow(() -> new BusinessException("Investigation with id [" + investigationId + "] not found", messages.prefixInvestigations));
    }

}
