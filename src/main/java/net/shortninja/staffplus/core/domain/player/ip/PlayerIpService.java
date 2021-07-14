package net.shortninja.staffplus.core.domain.player.ip;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.JavaUtils.convertIp;

@IocBean
public class PlayerIpService {

    private final PlayerIpRepository playerIpRepository;

    public PlayerIpService(PlayerIpRepository playerIpRepository) {
        this.playerIpRepository = playerIpRepository;
    }

    public void savePlayerIp(Player player) {
        String ipFromPlayer = BukkitUtils.getIpFromPlayer(player);
        playerIpRepository.save(player.getUniqueId(), player.getName(), ipFromPlayer);
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

}
