package net.shortninja.staffplus.core.investigate.gui;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.investigate.Investigation;
import net.shortninja.staffplus.core.investigate.bungee.events.InvestigationConcludedBungeeEvent;
import net.shortninja.staffplus.core.investigate.bungee.events.InvestigationPausedBungeeEvent;
import net.shortninja.staffplus.core.investigate.bungee.events.InvestigationStartedBungeeEvent;
import net.shortninja.staffplus.core.investigate.config.InvestigationConfiguration;
import net.shortninja.staffplus.core.investigate.database.investigation.InvestigationsRepository;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@IocBukkitListener
public class InvestigationChatNotifier implements Listener {

    private static final String INVESTIGATION_STARTED = "Investigation Started (ID=%investigationId%)";
    private static final String INVESTIGATION_CONCLUDED = "Investigation Concluded (ID=%investigationId%)";
    private static final String INVESTIGATION_PAUSED = "Investigation Paused (ID=%investigationId%)";

    private final PlayerManager playerManager;
    private final Messages messages;
    private final InvestigationConfiguration investigationConfiguration;
    private final InvestigationsRepository investigationsRepository;

    public InvestigationChatNotifier(PlayerManager playerManager, Messages messages, InvestigationConfiguration investigationConfiguration, InvestigationsRepository investigationsRepository) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.investigationConfiguration = investigationConfiguration;
        this.investigationsRepository = investigationsRepository;
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
        Bukkit.getScheduler().runTaskAsynchronously(TubingBukkitPlugin.getPlugin(), () -> {
            List<Investigation> investigation = investigationsRepository.findAllInvestigationForInvestigated(player.getUniqueId(), Collections.singletonList(InvestigationStatus.OPEN));
            if (!investigation.isEmpty() && StringUtils.isNotEmpty(messages.underInvestigationJoin)) {
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
            messages.sendGroupMessage(message, investigationConfiguration.getStaffNotificationPermission(), messages.prefixInvestigations);
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
        if (StringUtils.isNotEmpty(investigatedMessage) && investigationConfiguration.isInvestigatedChatMessageEnabled()) {
            investigation.getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
                .filter(SppPlayer::isOnline)
                .ifPresent(sppPlayer -> messages.send(sppPlayer.getPlayer(), investigatedMessage, messages.prefixInvestigations));
        }
    }

}
