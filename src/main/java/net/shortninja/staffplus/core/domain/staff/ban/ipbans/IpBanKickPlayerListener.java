package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpService;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto.IpBanBungeeDto;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.events.IpBanBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;
import net.shortninja.staffplusplus.ban.IIpBan;
import net.shortninja.staffplusplus.ban.IpBanEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanMessageStringUtil.replaceBanPlaceholders;
import static org.bukkit.Bukkit.getScheduler;

@IocBean
@IocListener
public class IpBanKickPlayerListener implements Listener {

    private final PlayerIpService playerIpService;
    private final PlayerManager playerManager;
    private final IpBanTemplateResolver ipBanTemplateResolver;
    private final Messages messages;

    public IpBanKickPlayerListener(PlayerIpService playerIpService, PlayerManager playerManager, IpBanTemplateResolver ipBanTemplateResolver, Messages messages) {
        this.playerIpService = playerIpService;
        this.playerManager = playerManager;
        this.ipBanTemplateResolver = ipBanTemplateResolver;
        this.messages = messages;
    }

    @EventHandler
    public void kickBannedPlayer(IpBanEvent ipBanEvent) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            IIpBan ban = ipBanEvent.getBan();
            List<PlayerIpRecord> playersToKick = ban.isSubnet() ? playerIpService.getMatchedBySubnet(ban.getIp()) : playerIpService.getMatchedByIp(ban.getIp());

            getScheduler().runTaskLater(StaffPlus.get(), () -> {
                List<SppPlayer> sppPlayers = playersToKick.stream().map(p -> playerManager.getOnlinePlayer(p.getPlayerUuid()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

                BanType banType = ban.getEndTimestamp().isPresent() ? BanType.TEMP_BAN : BanType.PERM_BAN;
                String template = ipBanTemplateResolver.resolveTemplate(ipBanEvent.getKickTemplate().orElse(null), banType);
                String banMessage = replaceBanPlaceholders(template, ban);
                sppPlayers.forEach(sppPlayer -> sppPlayer.getPlayer().kickPlayer(messages.colorize(banMessage)));
            }, 1);
        });

    }

    @EventHandler
    public void kickBannedPlayer(IpBanBungeeEvent ipBanEvent) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            IpBanBungeeDto ban = ipBanEvent.getBan();
            List<PlayerIpRecord> playersToKick = ban.isSubnet() ? playerIpService.getMatchedBySubnet(ban.getIp()) : playerIpService.getMatchedByIp(ban.getIp());

            getScheduler().runTaskLater(StaffPlus.get(), () -> {
                List<SppPlayer> sppPlayers = playersToKick.stream().map(p -> playerManager.getOnlinePlayer(p.getPlayerUuid()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

                BanType banType = ban.getEndTimestamp() != null ? BanType.TEMP_BAN : BanType.PERM_BAN;
                String template = ipBanTemplateResolver.resolveTemplate(ipBanEvent.getKickTemplate(), banType);
                String banMessage = replaceBanPlaceholders(template, ban);
                sppPlayers.forEach(sppPlayer -> sppPlayer.getPlayer().kickPlayer(messages.colorize(banMessage)));
            }, 1);
        });

    }
}
