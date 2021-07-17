package net.shortninja.staffplus.core.domain.player.ip;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;
import net.shortninja.staffplusplus.ips.IpHistoryClearedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.JavaUtils.convertIp;

@IocBean
public class PlayerIpService {

    private final PlayerIpRepository playerIpRepository;
    private final Options options;

    public PlayerIpService(PlayerIpRepository playerIpRepository, Options options) {
        this.playerIpRepository = playerIpRepository;
        this.options = options;
    }

    public void savePlayerIp(Player player) {
        String ipFromPlayer = BukkitUtils.getIpFromPlayer(player);
        playerIpRepository.save(player.getUniqueId(), player.getName(), ipFromPlayer, options.serverName);
    }

    public List<PlayerIpRecord> getMatchedBySubnet(String cidr) {
        String[] range = JavaUtils.cidrToIpRange(cidr);
        long lower = convertIp(range[0]);
        long upper = convertIp(range[1]);
        return playerIpRepository.findInSubnet(lower, upper);
    }

    public List<PlayerIpRecord> getMatchedByIp(String ipAddress) {
        return playerIpRepository.findWithIp(convertIp(ipAddress));
    }

    public void clearHistory(CommandSender sender, SppPlayer player) {
        playerIpRepository.deleteRecordsFor(player);
        BukkitUtils.sendEvent(new IpHistoryClearedEvent(sender, player));
    }
}
