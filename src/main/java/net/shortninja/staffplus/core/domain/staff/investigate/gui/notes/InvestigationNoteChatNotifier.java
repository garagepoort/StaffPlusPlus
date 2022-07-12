package net.shortninja.staffplus.core.domain.staff.investigate.gui.notes;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.config.InvestigationConfiguration;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationNoteCreatedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationNoteDeletedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBukkitListener
public class InvestigationNoteChatNotifier implements Listener {

    private static final String INVESTIGATION_NOTE_LINKED = "Added note to investigation (ID=%investigationId%)";
    private static final String INVESTIGATION_NOTE_DELETED = "Removed note from investigation (ID=%investigationId%)";

    private final PlayerManager playerManager;
    private final Messages messages;
    private final InvestigationConfiguration investigationConfiguration;

    public InvestigationNoteChatNotifier(PlayerManager playerManager, Messages messages, InvestigationConfiguration investigationConfiguration) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.investigationConfiguration = investigationConfiguration;
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
            messages.sendGroupMessage(message, investigationConfiguration.getStaffNotificationPermission(), messages.prefixInvestigations);
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
