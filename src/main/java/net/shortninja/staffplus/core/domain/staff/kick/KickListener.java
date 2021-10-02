package net.shortninja.staffplus.core.domain.staff.kick;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.kick.KickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class KickListener implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;

    public KickListener(Messages messages, PlayerManager playerManager) {
        this.messages = messages;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void kickPlayer(KickEvent kickEvent) {
        playerManager.getOnlinePlayer(kickEvent.getKick().getTargetUuid()).ifPresent(p -> {
            String message = messages.kickMessage
                .replace("%target%", kickEvent.getKick().getTargetName())
                .replace("%issuer%", kickEvent.getKick().getIssuerName())
                .replace("%reason%", kickEvent.getKick().getReason());
            p.getPlayer().kickPlayer(this.messages.colorize(message));
        });
    }

}
