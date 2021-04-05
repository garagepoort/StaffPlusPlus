package net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks;

import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;

import java.util.List;

public class IpDetector implements AltDetector {
    private final PlayerIpRepository playerIpRepository;

    public IpDetector(PlayerIpRepository playerIpRepository) {
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
