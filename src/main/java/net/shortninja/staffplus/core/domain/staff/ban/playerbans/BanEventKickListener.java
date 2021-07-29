package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.events.BanBungeeEvent;
import net.shortninja.staffplusplus.ban.BanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanMessageStringUtil.replaceBanPlaceholders;

@IocBean
@IocListener
public class BanEventKickListener implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;

    public BanEventKickListener(Messages messages, PlayerManager playerManager) {
        this.messages = messages;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void kickBannedPlayer(BanEvent banEvent) {
        playerManager.getOnlinePlayer(banEvent.getBan().getTargetUuid()).ifPresent(p -> {
            if (p.isOnline()) {
                String banMessage = replaceBanPlaceholders(banEvent.getBanMessage(), banEvent.getBan());
                p.getPlayer().kickPlayer(messages.colorize(banMessage));
            }
        });
    }

    @EventHandler
    public void kickBannedPlayerBungee(BanBungeeEvent banEvent) {
        playerManager.getOnlinePlayer(banEvent.getBan().getTargetUuid()).ifPresent(p -> {
            if (p.isOnline()) {
                String banMessage = replaceBanPlaceholders(banEvent.getBan().getBanMessage(), banEvent.getBan());
                p.getPlayer().kickPlayer(messages.colorize(banMessage));
            }
        });
    }

}
