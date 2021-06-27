package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import net.shortninja.staffplusplus.altdetect.AltDetectResultType;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;
import net.shortninja.staffplusplus.altdetect.IAltDetectResult;

import java.util.List;
import java.util.UUID;

import static net.shortninja.staffplusplus.altdetect.AltDetectResultType.SAME_IP;

public class AltDetectResult implements IAltDetectResult {

    private final UUID playerCheckedUuid;
    private final String playerCheckedName;
    private final UUID playerMatchedUuid;
    private final String playerMatchedName;
    private final boolean ipMatched;
    private final AltDetectTrustLevel altDetectTrustLevel;
    private final List<AltDetectResultType> altDetectResults;

    public AltDetectResult(UUID playerCheckedUuid, String playerCheckedName, UUID playerMatchedUuid, String playerMatchedName, List<AltDetectResultType> altDetectResults) {
        this.playerCheckedUuid = playerCheckedUuid;
        this.playerCheckedName = playerCheckedName;
        this.playerMatchedUuid = playerMatchedUuid;
        this.playerMatchedName = playerMatchedName;
        this.altDetectResults = altDetectResults;
        this.altDetectTrustLevel = AltDetectTrustLevel.fromScore(altDetectResults.stream().mapToInt(AltDetectResultType::getScore).sum());
        this.ipMatched = altDetectResults.contains(SAME_IP);
    }

    @Override
    public boolean isIpMatched() {
        return ipMatched;
    }

    @Override
    public UUID getPlayerCheckedUuid() {
        return playerCheckedUuid;
    }

    @Override
    public String getPlayerCheckedName() {
        return playerCheckedName;
    }

    @Override
    public UUID getPlayerMatchedUuid() {
        return playerMatchedUuid;
    }

    @Override
    public String getPlayerMatchedName() {
        return playerMatchedName;
    }

    @Override
    public AltDetectTrustLevel getAltDetectTrustLevel() {
        return altDetectTrustLevel;
    }

    @Override
    public List<AltDetectResultType> getAltDetectResultTypes() {
        return altDetectResults;
    }
}
