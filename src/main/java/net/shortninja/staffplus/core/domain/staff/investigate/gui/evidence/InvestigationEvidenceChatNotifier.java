package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.IInvestigationEvidence;
import net.shortninja.staffplusplus.investigate.InvestigationEvidenceLinkedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationEvidenceUnlinkedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBean
@IocListener
public class InvestigationEvidenceChatNotifier implements Listener {

    private static final String INVESTIGATION_EVIDENCE_LINKED = "Linked evidence %evidenceType% (ID=%evidenceId%) to investigation (ID=%investigationId%)";
    private static final String INVESTIGATION_EVIDENCE_UNLINKED = "Removed evidence %evidenceType% (ID=%evidenceId%) from investigation (ID=%investigationId%)";

    private final PlayerManager playerManager;
    private final Messages messages;
    private final Options options;

    public InvestigationEvidenceChatNotifier(PlayerManager playerManager, Messages messages, Options options) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.options = options;
    }

    @EventHandler
    public void notifyEvidenceLinked(InvestigationEvidenceLinkedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, event.getInvestigationEvidence(), INVESTIGATION_EVIDENCE_LINKED);
        sendStaffNotifications(investigation, event.getInvestigationEvidence(), messages.investigationEvidenceLinked);
    }

    @EventHandler
    public void notifyEvidenceUnlinked(InvestigationEvidenceUnlinkedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, event.getInvestigationEvidence(), INVESTIGATION_EVIDENCE_UNLINKED);
        sendStaffNotifications(investigation, event.getInvestigationEvidence(), messages.investigationEvidenceUnlinked);
    }

    private void sendStaffNotifications(IInvestigation investigation, IInvestigationEvidence evidence, String messageToSend) {
        if (messageToSend != null) {
            String message = messageToSend
                .replace("%investigationId%", String.valueOf(investigation.getId()))
                .replace("%investigator%", investigation.getInvestigatorName())
                .replace("%evidenceId%", String.valueOf(evidence.getEvidenceId()))
                .replace("%evidenceType%", evidence.getEvidenceType().name())
                .replace("%investigated%", investigation.getInvestigatedName());
            messages.sendGroupMessage(message, options.investigationConfiguration.getStaffNotificationPermission(), messages.prefixInvestigations);
        }
    }

    private void sendInvestigatorMessage(IInvestigation investigation, IInvestigationEvidence evidence, String investigatorMessage) {
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        investigator.map(SppPlayer::getPlayer).ifPresent(p -> {
            String message = investigatorMessage
                .replace("%investigationId%", String.valueOf(investigation.getId()))
                .replace("%evidenceId%", String.valueOf(evidence.getEvidenceId()))
                .replace("%evidenceType%", evidence.getEvidenceType().name());
            messages.send(p, message, messages.prefixInvestigations);
        });
    }

}
