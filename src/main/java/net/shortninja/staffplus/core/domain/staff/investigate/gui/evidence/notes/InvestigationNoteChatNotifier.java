package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.investigate.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBean
@IocListener
public class InvestigationNoteChatNotifier implements Listener {

    private static final String INVESTIGATION_NOTE_LINKED = "Added note to investigation (ID=%investigationId%)";
    private static final String INVESTIGATION_NOTE_DELETED = "Removed note from investigation (ID=%investigationId%)";

    private final PlayerManager playerManager;
    private final Messages messages;
    private final Options options;

    public InvestigationNoteChatNotifier(PlayerManager playerManager, Messages messages, Options options) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.options = options;
    }

    @EventHandler
    public void notifyNoteCreated(InvestigationNoteCreatedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_NOTE_LINKED);
        sendStaffNotifications(investigation, messages.investigationNoteAdded);
    }

    @EventHandler
    public void notifyNoteDeleted(InvestigationNoteDeletedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_NOTE_DELETED);
        sendStaffNotifications(investigation, messages.investigationNoteDeleted);
    }

    private void sendStaffNotifications(IInvestigation investigation, String messageToSend) {
        if (messageToSend != null) {
            String message = messageToSend
                .replace("%investigationId%", String.valueOf(investigation.getId()))
                .replace("%investigator%", investigation.getInvestigatorName())
                .replace("%investigated%", investigation.getInvestigatedName().orElse("Unknown"));
            messages.sendGroupMessage(message, options.investigationConfiguration.getStaffNotificationPermission(), messages.prefixInvestigations);
        }
    }

    private void sendInvestigatorMessage(IInvestigation investigation, String investigatorMessage) {
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        investigator.map(SppPlayer::getPlayer).ifPresent(p -> {
            String message = investigatorMessage
                .replace("%investigationId%", String.valueOf(investigation.getId()));
            messages.send(p, message, messages.prefixInvestigations);
        });
    }

}
