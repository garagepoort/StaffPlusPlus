package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplusplus.warnings.WarningCreatedEvent;
import net.shortninja.staffplusplus.warnings.WarningExpiredEvent;
import net.shortninja.staffplusplus.warnings.WarningRemovedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

import static net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.WarnMessageStringUtil.replaceWarningPlaceholders;

@IocBean
@IocListener
public class WarningChatNotifier implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;
    private final WarningConfiguration warningConfiguration;

    public WarningChatNotifier(Messages messages, PlayerManager playerManager, WarningConfiguration warningConfiguration) {
        this.messages = messages;
        this.playerManager = playerManager;
        this.warningConfiguration = warningConfiguration;
    }

    @EventHandler
    public void notifyWarningIssued(WarningCreatedEvent event) {
        IWarning warning = event.getWarning();
        Optional<SppPlayer> issuer = playerManager.getOnlinePlayer(warning.getIssuerUuid());
        issuer.ifPresent(p -> messages.send(p.getPlayer(), replaceWarningPlaceholders(messages.warned, warning), messages.prefixWarnings));

        playerManager.getOnOrOfflinePlayer(warning.getTargetUuid())
            .filter(SppPlayer::isOnline)
            .ifPresent(p -> {
                Player player = p.getPlayer();
                messages.send(player, replaceWarningPlaceholders(messages.warn, warning), messages.prefixWarnings);
                warningConfiguration.getSound().ifPresent(s -> s.play(player));
            });

        messages.sendGroupMessage(replaceWarningPlaceholders(messages.warnedAnnouncement, warning), warningConfiguration.getNotificationsPermission(), messages.prefixWarnings);
    }

    @EventHandler
    public void notifyWarningRemoved(WarningRemovedEvent event) {
        IWarning warning = event.getWarning();
        playerManager.getOnlinePlayer(warning.getIssuerUuid())
            .ifPresent(p -> messages.send(p.getPlayer(), "&2Warning has been removed", messages.prefixWarnings));
    }

    @EventHandler
    public void notifyWarningExpired(WarningExpiredEvent event) {
        IWarning warning = event.getWarning();
        playerManager.getOnlinePlayer(warning.getIssuerUuid())
            .ifPresent(p -> messages.send(p.getPlayer(), "&2Warning has been expired", messages.prefixWarnings));
    }

}
