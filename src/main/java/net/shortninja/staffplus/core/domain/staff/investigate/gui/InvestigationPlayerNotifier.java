package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBean
@IocListener
public class InvestigationPlayerNotifier implements Listener {

    private final PlayerManager playerManager;
    private final Messages messages;

    public InvestigationPlayerNotifier(PlayerManager playerManager, Messages messages) {
        this.playerManager = playerManager;
        this.messages = messages;
    }

    @EventHandler
    public void notifyInvestigationStarted(InvestigationStartedEvent investigationStartedEvent) {
        IInvestigation investigation = investigationStartedEvent.getInvestigation();
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        investigator.map(SppPlayer::getPlayer).ifPresent(p -> messages.send(p, "Investigation Started", messages.prefixInvestigations));

        if (StringUtils.isNotEmpty(messages.investigatedInvestigationStarted)) {
            playerManager.getOnlinePlayer(investigation.getInvestigatedUuid())
                .filter(SppPlayer::isOnline)
                .ifPresent(sppPlayer -> messages.send(sppPlayer.getPlayer(), messages.investigatedInvestigationStarted, messages.prefixInvestigations));
        }
    }

    @EventHandler
    public void notifyInvestigationConcluded(InvestigationConcludedEvent investigationConcludedEvent) {
        IInvestigation investigation = investigationConcludedEvent.getInvestigation();
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        investigator.map(SppPlayer::getPlayer).ifPresent(p -> messages.send(p, "Investigation Concluded", messages.prefixInvestigations));

        if (StringUtils.isNotEmpty(messages.investigatedInvestigationConcluded)) {
            playerManager.getOnlinePlayer(investigation.getInvestigatedUuid())
                .filter(SppPlayer::isOnline)
                .ifPresent(sppPlayer -> messages.send(sppPlayer.getPlayer(), messages.investigatedInvestigationConcluded, messages.prefixInvestigations));
        }
    }

}
