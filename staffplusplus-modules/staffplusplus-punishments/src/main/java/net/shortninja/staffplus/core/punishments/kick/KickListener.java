package net.shortninja.staffplus.core.punishments.kick;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.kick.KickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener
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
