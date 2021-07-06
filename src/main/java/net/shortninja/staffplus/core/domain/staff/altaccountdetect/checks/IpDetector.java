package net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks;

import net.shortninja.staffplusplus.altdetect.AltDetectResultType;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;

import java.util.List;

import static net.shortninja.staffplusplus.altdetect.AltDetectResultType.SAME_IP;

public class IpDetector implements AltDetector {
    private final PlayerIpRepository playerIpRepository;

    public IpDetector(PlayerIpRepository playerIpRepository) {
        this.playerIpRepository = playerIpRepository;
    }

    @Override
    public AltDetectResultType getResult(AltDetectInfo altDetectInfo, SppPlayer sppPlayer) {
        List<String> ips = playerIpRepository.getIps(sppPlayer.getId());
        if (altDetectInfo.getIp() != null && ips.contains(altDetectInfo.getIp())) {
            return SAME_IP;
        }
        return null;
    }
}
