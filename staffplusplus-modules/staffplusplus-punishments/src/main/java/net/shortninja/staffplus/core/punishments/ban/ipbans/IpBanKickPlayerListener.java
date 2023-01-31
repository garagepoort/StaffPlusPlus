package net.shortninja.staffplus.core.punishments.ban.ipbans;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.PlaceholderService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpService;
import net.shortninja.staffplus.core.punishments.ban.ipbans.bungee.dto.IpBanBungeeDto;
import net.shortninja.staffplus.core.punishments.ban.ipbans.bungee.events.IpBanBungeeEvent;
import net.shortninja.staffplus.core.punishments.ban.playerbans.BanType;
import net.shortninja.staffplusplus.ban.IIpBan;
import net.shortninja.staffplusplus.ban.IpBanEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.punishments.ban.ipbans.IpBanMessageStringUtil.replaceBanPlaceholders;
import static org.bukkit.Bukkit.getScheduler;

@IocBukkitListener
public class IpBanKickPlayerListener implements Listener {

    private final PlayerIpService playerIpService;
    private final PlayerManager playerManager;
    private final IpBanTemplateResolver ipBanTemplateResolver;
    private final Messages messages;
    private final PlaceholderService placeholderService;

    public IpBanKickPlayerListener(PlayerIpService playerIpService, PlayerManager playerManager, IpBanTemplateResolver ipBanTemplateResolver, Messages messages, PlaceholderService placeholderService) {
        this.playerIpService = playerIpService;
        this.playerManager = playerManager;
        this.ipBanTemplateResolver = ipBanTemplateResolver;
        this.messages = messages;
        this.placeholderService = placeholderService;
    }

    @EventHandler
    public void kickBannedPlayer(IpBanEvent ipBanEvent) {
        getScheduler().runTaskAsynchronously(TubingBukkitPlugin.getPlugin(), () -> {
            IIpBan ban = ipBanEvent.getBan();
            List<PlayerIpRecord> playersToKick = ban.isSubnet() ? playerIpService.getMatchedBySubnet(ban.getIp()) : playerIpService.getMatchedByIp(ban.getIp());

            getScheduler().runTaskLater(TubingBukkitPlugin.getPlugin(), () -> {
                List<SppPlayer> sppPlayers = playersToKick.stream().map(p -> playerManager.getOnlinePlayer(p.getPlayerUuid()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

                BanType banType = ban.getEndTimestamp().isPresent() ? BanType.TEMP_BAN : BanType.PERM_BAN;
                String template = ipBanTemplateResolver.resolveTemplate(ipBanEvent.getKickTemplate().orElse(null), banType);
                kickPlayers(sppPlayers, IpBanMessageStringUtil.replaceBanPlaceholders(template, ban));
            }, 1);
        });
    }

    @EventHandler
    public void kickBannedPlayer(IpBanBungeeEvent ipBanEvent) {
        getScheduler().runTaskAsynchronously(TubingBukkitPlugin.getPlugin(), () -> {
            IpBanBungeeDto ban = ipBanEvent.getBan();
            List<PlayerIpRecord> playersToKick = ban.isSubnet() ? playerIpService.getMatchedBySubnet(ban.getIp()) : playerIpService.getMatchedByIp(ban.getIp());

            getScheduler().runTaskLater(TubingBukkitPlugin.getPlugin(), () -> {
                List<SppPlayer> sppPlayers = playersToKick.stream().map(p -> playerManager.getOnlinePlayer(p.getPlayerUuid()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

                BanType banType = ban.getEndTimestamp() != null ? BanType.TEMP_BAN : BanType.PERM_BAN;
                String template = ipBanTemplateResolver.resolveTemplate(ipBanEvent.getKickTemplate(), banType);

                kickPlayers(sppPlayers, IpBanMessageStringUtil.replaceBanPlaceholders(template, ban));
            }, 1);
        });
    }

    private void kickPlayers(List<SppPlayer> sppPlayers, String s) {
        sppPlayers.forEach(sppPlayer -> {
            String banMessage;
            banMessage = placeholderService.setPlaceholders(sppPlayer.getOfflinePlayer(), s);
            sppPlayer.getPlayer().kickPlayer(messages.colorize(banMessage));
        });
    }
}
