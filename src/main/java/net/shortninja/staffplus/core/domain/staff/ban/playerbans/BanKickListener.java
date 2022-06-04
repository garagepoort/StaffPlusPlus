package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.PlaceholderService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.dto.BanBungeeDto;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.events.BanBungeeEvent;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.IBan;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanMessageStringUtil.replaceBanPlaceholders;

@IocListener
public class BanKickListener implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;
    private final BanService banService;
    private final BanTemplateResolver banTemplateResolver;
    private final PlaceholderService placeholderService;

    public BanKickListener(Messages messages, PlayerManager playerManager, BanService banService, BanTemplateResolver banTemplateResolver, PlaceholderService placeholderService) {
        this.messages = messages;
        this.playerManager = playerManager;
        this.banService = banService;
        this.banTemplateResolver = banTemplateResolver;
        this.placeholderService = placeholderService;
    }

    @EventHandler
    public void kickBannedPlayer(BanEvent banEvent) {
        playerManager.getOnlinePlayer(banEvent.getBan().getTargetUuid()).ifPresent(p -> {
            if (p.isOnline()) {
                String banMessage = getBanMessage(banEvent.getBan().getTargetUuid(), banEvent.getBan(), banEvent.getBanMessage());
                p.getPlayer().kickPlayer(messages.colorize(banMessage));
            }
        });
    }

    @EventHandler
    public void kickBannedPlayerBungee(BanBungeeEvent banEvent) {
        playerManager.getOnlinePlayer(banEvent.getBan().getTargetUuid()).ifPresent(p -> {
            if (p.isOnline()) {
                String banMessage = getBanMessage(banEvent.getBan().getTargetUuid(), banEvent.getBan(), banEvent.getBan().getBanMessage());
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

            String banMessage = getBanMessage(event.getUniqueId(), ban, templateMessage);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
        }
    }

    private String getBanMessage(UUID playerUuid, IBan ban, String templateMessage) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUuid);
        return player.isPresent() ?
            placeholderService.setPlaceholders(player.get().getOfflinePlayer(), replaceBanPlaceholders(templateMessage, ban)) :
            replaceBanPlaceholders(templateMessage, ban);
    }

    private String getBanMessage(UUID playerUuid, BanBungeeDto ban, String templateMessage) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUuid);
        return player.isPresent() ?
            placeholderService.setPlaceholders(player.get().getOfflinePlayer(), replaceBanPlaceholders(templateMessage, ban)) :
            replaceBanPlaceholders(templateMessage, ban);
    }
}
