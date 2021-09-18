package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.events.BanBungeeEvent;
import net.shortninja.staffplusplus.ban.BanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Optional;

import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanMessageStringUtil.replaceBanPlaceholders;

@IocBean
@IocListener
public class BanKickListener implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;
    private final BanService banService;
    private final BanTemplateResolver banTemplateResolver;

    public BanKickListener(Messages messages, PlayerManager playerManager, BanService banService, BanTemplateResolver banTemplateResolver) {
        this.messages = messages;
        this.playerManager = playerManager;
        this.banService = banService;
        this.banTemplateResolver = banTemplateResolver;
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        Optional<Ban> optionalBan = banService.getBanByBannedUuid(event.getUniqueId());
        if (optionalBan.isPresent()) {
            Ban ban = optionalBan.get();
            BanType banType = ban.getEndTimestamp() == null ? BanType.PERM_BAN : BanType.TEMP_BAN;

            String templateMessage;
            if (ban.getTemplate().isPresent() && banTemplateResolver.hasTemplate(ban.getTemplate().get())) {
                templateMessage = banTemplateResolver.resolveTemplate(ban.getReason(), ban.getTemplate().get(), banType);
            } else {
                templateMessage = banTemplateResolver.resolveTemplate(ban.getReason(), null, banType);
            }

            String banMessage = replaceBanPlaceholders(templateMessage, ban);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
        }
    }

}
