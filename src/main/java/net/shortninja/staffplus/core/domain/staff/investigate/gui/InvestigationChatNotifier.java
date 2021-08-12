package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationConcludedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationPausedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationStartedBungeeEvent;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBean
@IocListener
public class InvestigationChatNotifier implements Listener {

    private static final String INVESTIGATION_STARTED = "Investigation Started (ID=%investigationId%)";
    private static final String INVESTIGATION_CONCLUDED = "Investigation Concluded (ID=%investigationId%)";
    private static final String INVESTIGATION_PAUSED = "Investigation Paused (ID=%investigationId%)";

    private final PlayerManager playerManager;
    private final Messages messages;
    private final Options options;

    public InvestigationChatNotifier(PlayerManager playerManager, Messages messages, Options options) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.options = options;
    }

    @EventHandler
    public void notifyInvestigationStarted(InvestigationStartedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_STARTED);
        sendInvestigatedMessage(investigation, messages.investigatedInvestigationStarted);
        sendStaffNotifications(investigation, messages.investigationStaffNotificationsStarted);
    }

    @EventHandler
    public void notifyInvestigationStarted(InvestigationStartedBungeeEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_STARTED);
        sendInvestigatedMessage(investigation, messages.investigatedInvestigationStarted);
        sendStaffNotifications(investigation, messages.investigationStaffNotificationsStarted);
    }

    @EventHandler
    public void notifyInvestigationConcluded(InvestigationConcludedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_CONCLUDED);
        sendInvestigatedMessage(investigation, messages.investigatedInvestigationConcluded);
        sendStaffNotifications(investigation, messages.investigationStaffNotificationsConcluded);
    }

    @EventHandler
    public void notifyInvestigationConcluded(InvestigationConcludedBungeeEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_CONCLUDED);
        sendInvestigatedMessage(investigation, messages.investigatedInvestigationConcluded);
        sendStaffNotifications(investigation, messages.investigationStaffNotificationsConcluded);
    }

    @EventHandler
    public void notifyInvestigationPaused(InvestigationPausedEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_PAUSED);
        sendInvestigatedMessage(investigation, messages.investigatedInvestigationPaused);
        sendStaffNotifications(investigation, messages.investigationStaffNotificationsPaused);
    }

    @EventHandler
    public void notifyInvestigationPaused(InvestigationPausedBungeeEvent event) {
        IInvestigation investigation = event.getInvestigation();
        sendInvestigatorMessage(investigation, INVESTIGATION_PAUSED);
        sendInvestigatedMessage(investigation, messages.investigatedInvestigationPaused);
        sendStaffNotifications(investigation, messages.investigationStaffNotificationsPaused);
    }

    @EventHandler
    public void notifyUnderInvestigationOnJoin(StaffPlusPlusJoinedEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            if (event.getPlayerSession().isUnderInvestigation() && StringUtils.isNotEmpty(messages.underInvestigationJoin)) {
                messages.send(player, messages.underInvestigationJoin, messages.prefixInvestigations);
            }
        });
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
            String message = investigatorMessage.replace("%investigationId%", String.valueOf(investigation.getId()));
            messages.send(p, message, messages.prefixInvestigations);
        });
    }

    private void sendInvestigatedMessage(IInvestigation investigation, String investigatedMessage) {
        if (StringUtils.isNotEmpty(investigatedMessage) && options.investigationConfiguration.isInvestigatedChatMessageEnabled()) {
            investigation.getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
                .filter(SppPlayer::isOnline)
                .ifPresent(sppPlayer -> messages.send(sppPlayer.getPlayer(), investigatedMessage, messages.prefixInvestigations));
        }
    }

}
