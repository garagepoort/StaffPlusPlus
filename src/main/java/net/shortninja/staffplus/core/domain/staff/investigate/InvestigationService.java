package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.investigate.config.InvestigationConfiguration;
import net.shortninja.staffplus.core.domain.staff.investigate.database.investigation.InvestigationsRepository;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

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
    private final PermissionHandler permissionHandler;
    private final InvestigationConfiguration investigationConfiguration;

    public InvestigationService(InvestigationsRepository investigationsRepository, Options options, Messages messages, PermissionHandler permissionHandler, InvestigationConfiguration investigationConfiguration) {
        this.investigationsRepository = investigationsRepository;
        this.options = options;
        this.messages = messages;
        this.permissionHandler = permissionHandler;
        this.investigationConfiguration = investigationConfiguration;
    }

    public void startInvestigation(Player investigator, SppPlayer investigated) {
        permissionHandler.validate(investigator, investigationConfiguration.getInvestigatePermission());
        validateInvestigationStart(investigator, investigated);

        Investigation investigation = new Investigation(investigator.getName(), investigator.getUniqueId(), investigated.getUsername(), investigated.getId(), options.serverName);
        int id = investigationsRepository.addInvestigation(investigation);
        investigation.setId(id);
        sendEvent(new InvestigationStartedEvent(investigation));
    }

    public void startInvestigation(Player investigator) {
        permissionHandler.validate(investigator, investigationConfiguration.getInvestigatePermission());
        validateInvestigationStart(investigator);

        Investigation investigation = new Investigation(investigator.getName(), investigator.getUniqueId(), options.serverName);
        int id = investigationsRepository.addInvestigation(investigation);
        investigation.setId(id);
        sendEvent(new InvestigationStartedEvent(investigation));
    }

    public void resumeInvestigation(Player investigator, int investigationId) {
        permissionHandler.validate(investigator, investigationConfiguration.getInvestigatePermission());
        validateInvestigationStart(investigator);

        Investigation investigation = getInvestigation(investigationId);
        if (investigation.getStatus() != PAUSED) {
            throw new BusinessException(messages.get("investigation.error-cannot-resume-not-paused"), messages.prefixInvestigations);
        }
        investigation.setStatus(OPEN);
        investigation.setInvestigatorName(investigator.getName());
        investigation.setInvestigatorUuid(investigator.getUniqueId());
        investigationsRepository.updateInvestigation(investigation);
        sendEvent(new InvestigationStartedEvent(investigation));
    }

    public void resumeInvestigation(Player investigator, SppPlayer investigated) {
        permissionHandler.validate(investigator, investigationConfiguration.getInvestigatePermission());
        validateInvestigationStart(investigator, investigated);

        Investigation investigation = investigationsRepository.findInvestigationForInvestigated(investigator.getUniqueId(), investigated.getId(), Collections.singletonList(PAUSED))
            .orElseThrow(() -> new BusinessException(messages.get("investigation.error-no-paused-found"), messages.prefixInvestigations));
        investigation.setStatus(OPEN);
        investigation.setInvestigatorName(investigator.getName());
        investigation.setInvestigatorUuid(investigator.getUniqueId());
        investigationsRepository.updateInvestigation(investigation);
        sendEvent(new InvestigationStartedEvent(investigation));
    }

    public Optional<Investigation> getPausedInvestigation(Player investigator, SppPlayer investigated) {
        return investigationsRepository.findInvestigationForInvestigated(investigator.getUniqueId(), investigated.getId(), Collections.singletonList(PAUSED));
    }

    public List<Investigation> getPausedInvestigations(Player investigator) {
        return investigationsRepository.findAllInvestigationsForInvestigator(investigator.getUniqueId(), Collections.singletonList(PAUSED));
    }

    public List<Investigation> getPausedInvestigations(Player investigator, int offset, int amount) {
        return investigationsRepository.findAllInvestigationsForInvestigator(investigator.getUniqueId(), Collections.singletonList(PAUSED), offset, amount);
    }

    public void concludeInvestigation(Player investigator) {
        permissionHandler.validate(investigator, investigationConfiguration.getInvestigatePermission());
        Investigation investigation = investigationsRepository.getInvestigationForInvestigator(investigator.getUniqueId(), Collections.singletonList(OPEN))
            .orElseThrow(() -> new BusinessException(messages.get("investigation.error-none-running"), messages.prefixInvestigations));
        investigation.setConclusionDate(System.currentTimeMillis());
        investigation.setStatus(InvestigationStatus.CONCLUDED);

        investigationsRepository.updateInvestigation(investigation);
        sendEvent(new InvestigationConcludedEvent(investigation));
    }

    public void concludeInvestigation(Player investigator, int investigationId) {
        permissionHandler.validate(investigator, investigationConfiguration.getInvestigatePermission());
        Investigation investigation = investigationsRepository.findInvestigation(investigationId)
            .orElseThrow(() -> new BusinessException(messages.get("investigation.error-not-found"), messages.prefixInvestigations));

        if (investigation.getStatus() == OPEN && !investigation.getInvestigatorUuid().equals(investigator.getUniqueId())) {
            throw new BusinessException(messages.get("investigation.error-cannot-conclude-ongoing"), messages.prefixInvestigations);
        }
        investigation.setConclusionDate(System.currentTimeMillis());
        investigation.setStatus(InvestigationStatus.CONCLUDED);

        investigationsRepository.updateInvestigation(investigation);
        sendEvent(new InvestigationConcludedEvent(investigation));
    }

    public void pauseInvestigation(Player investigator) {
        Investigation investigation = investigationsRepository.getInvestigationForInvestigator(investigator.getUniqueId(), Collections.singletonList(OPEN))
            .orElseThrow(() -> new BusinessException(messages.get("investigation.error-none-running"), messages.prefixInvestigations));
        investigation.setStatus(PAUSED);

        investigationsRepository.updateInvestigation(investigation);
        sendEvent(new InvestigationPausedEvent(investigation));
    }

    public void tryPausingInvestigation(Player investigator) {
        investigationsRepository.getInvestigationForInvestigator(investigator.getUniqueId(), Collections.singletonList(OPEN))
            .ifPresent(investigation -> {
                investigation.setStatus(PAUSED);
                investigationsRepository.updateInvestigation(investigation);
                sendEvent(new InvestigationPausedEvent(investigation));
            });
    }

    public void pauseInvestigationsForInvestigated(Player investigated) {
        List<Investigation> investigations = investigationsRepository.getInvestigationsForInvestigated(investigated.getUniqueId(), Collections.singletonList(OPEN));
        for (Investigation investigation : investigations) {
            investigation.setStatus(PAUSED);
            investigationsRepository.updateInvestigation(investigation);
            sendEvent(new InvestigationPausedEvent(investigation));
        }
    }

    public List<Investigation> getAllInvestigations(int offset, int amount) {
        return investigationsRepository.getAllInvestigations(offset, amount);
    }

    public List<Investigation> getInvestigationsForInvestigated(SppPlayer sppPlayer, int offset, int amount) {
        return investigationsRepository.getInvestigationsForInvestigated(sppPlayer.getId(), offset, amount);
    }

    public Investigation getInvestigation(int investigationId) {
        return investigationsRepository.findInvestigation(investigationId)
            .orElseThrow(() -> new BusinessException(messages.get("investigation.error-not-found-with-id", "%investigationId%", Integer.toString(investigationId)), messages.prefixInvestigations));
    }

    private void validateInvestigationStart(Player investigator, SppPlayer investigated) {
        if (!investigated.isOnline() && !investigationConfiguration.isAllowOfflineInvestigation()) {
            throw new BusinessException(messages.get("investigation.error-offline-not-allowed"), messages.prefixInvestigations);
        }
        investigationsRepository.getInvestigationForInvestigator(investigator.getUniqueId(), Collections.singletonList(OPEN)).ifPresent(investigation1 -> {
            throw new BusinessException(messages.get("investigation.error-already-running"), messages.prefixInvestigations);
        });
        List<Investigation> runningInvestigations = investigationsRepository.getInvestigationsForInvestigated(investigated.getId(), Collections.singletonList(OPEN));
        if (investigationConfiguration.getMaxConcurrentInvestigation() > 0 && runningInvestigations.size() >= investigationConfiguration.getMaxConcurrentInvestigation()) {
            throw new BusinessException(messages.get("investigation.error-too-many-running"), messages.prefixInvestigations);
        }
    }

    private void validateInvestigationStart(Player investigator) {
        investigationsRepository.getInvestigationForInvestigator(investigator.getUniqueId(), Collections.singletonList(OPEN)).ifPresent(investigation1 -> {
            throw new BusinessException(messages.get("investigation.error-already-running"), messages.prefixInvestigations);
        });
    }

}
