package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationConcludedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationPausedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationStartedBungeeEvent;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

@IocBean
@IocListener
public class InvestigationChatNotifier implements Listener {

    private static final String INVESTIGATION_STARTED = "Investigation Started";
    private static final String INVESTIGATION_CONCLUDED = "Investigation Concluded";
    private static final String INVESTIGATION_PAUSED = "Investigation Paused";

    private final PlayerManager playerManager;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final Options options;

    public InvestigationChatNotifier(PlayerManager playerManager, Messages messages, SessionManagerImpl sessionManager, Options options) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.options = options;
    }

    @EventHandler
    public void notifyInvestigationStarted(InvestigationStartedEvent event) {
        sendChat(event.getInvestigation(), INVESTIGATION_STARTED, messages.investigatedInvestigationStarted, messages.investigationStaffNotificationsStarted);
    }

    @EventHandler
    public void notifyInvestigationStarted(InvestigationStartedBungeeEvent event) {
        sendChat(event.getInvestigation(), INVESTIGATION_STARTED, messages.investigatedInvestigationStarted, messages.investigationStaffNotificationsStarted);
    }

    @EventHandler
    public void notifyInvestigationConcluded(InvestigationConcludedEvent event) {
        sendChat(event.getInvestigation(), INVESTIGATION_CONCLUDED, messages.investigatedInvestigationConcluded, messages.investigationStaffNotificationsConcluded);
    }

    @EventHandler
    public void notifyInvestigationConcluded(InvestigationConcludedBungeeEvent event) {
        sendChat(event.getInvestigation(), INVESTIGATION_CONCLUDED, messages.investigatedInvestigationConcluded, messages.investigationStaffNotificationsConcluded);
    }

    @EventHandler
    public void notifyInvestigationPaused(InvestigationPausedEvent event) {
        sendChat(event.getInvestigation(), INVESTIGATION_PAUSED, messages.investigatedInvestigationPaused, messages.investigationStaffNotificationsPaused);
    }

    @EventHandler
    public void notifyInvestigationPaused(InvestigationPausedBungeeEvent event) {
        sendChat(event.getInvestigation(), INVESTIGATION_PAUSED, messages.investigatedInvestigationPaused, messages.investigationStaffNotificationsPaused);
    }

    @EventHandler
    public void notifyUnderInvestigationOnJoin(PlayerJoinEvent playerJoinEvent) {
        PlayerSession playerSession = sessionManager.get(playerJoinEvent.getPlayer().getUniqueId());
        if (playerSession.isUnderInvestigation() && StringUtils.isNotEmpty(messages.underInvestigationJoin)) {
            messages.send(playerJoinEvent.getPlayer(), messages.underInvestigationJoin, messages.prefixInvestigations);
        }
    }

    private void sendChat(IInvestigation investigation, String investigatorMessage, String investigatedMessage, String staffNotificationMessage) {
        sendMessage(investigation, investigatorMessage, investigatedMessage);
        sendStaffNotifications(investigation, staffNotificationMessage);
    }

    private void sendStaffNotifications(IInvestigation investigation, String messageToSend) {
        if (messageToSend != null) {
            String message = messageToSend
                .replace("%investigator%", investigation.getInvestigatorName())
                .replace("%investigated%", investigation.getInvestigatedName());
            messages.sendGroupMessage(message, options.investigationConfiguration.getStaffNotificationPermission(), messages.prefixInvestigations);
        }
    }

    private void sendMessage(IInvestigation investigation, String investigatorMessage, String investigatedMessage) {
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        investigator.map(SppPlayer::getPlayer).ifPresent(p -> messages.send(p, investigatorMessage, messages.prefixInvestigations));

        if (StringUtils.isNotEmpty(investigatedMessage)) {
            playerManager.getOnlinePlayer(investigation.getInvestigatedUuid())
                .filter(SppPlayer::isOnline)
                .ifPresent(sppPlayer -> messages.send(sppPlayer.getPlayer(), investigatedMessage, messages.prefixInvestigations));
        }
    }

}
