package net.shortninja.staffplus.staff.altaccountdetect.checks;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;

import java.util.List;

public class IpCheck implements AltCheck {
    private final PlayerIpRepository playerIpRepository;

    public IpCheck(PlayerIpRepository playerIpRepository) {
        this.playerIpRepository = playerIpRepository;
    }

    @Override
    public int getScore(AltDetectInfo altDetectInfo, SppPlayer sppPlayer) {
        List<String> ips = playerIpRepository.getIps(sppPlayer.getId());
        if (ips.contains(altDetectInfo.getIp())) {
            return 1;
        }
        return 0;
    }
}
