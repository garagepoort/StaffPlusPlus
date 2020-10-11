package net.shortninja.staffplus.staff.altaccountdetect;

import net.shortninja.staffplus.unordered.altdetect.AltAccountTrustScore;
import net.shortninja.staffplus.unordered.altdetect.IAltDetectResult;

import java.util.UUID;

public class AltDetectResult implements IAltDetectResult {

    private UUID playerCheckedUuid;
    private String playerCheckedName;
    private UUID playerMatchedUuid;
    private String playerMatchedName;
    private AltAccountTrustScore altAccountTrustScore;

    public AltDetectResult(UUID playerCheckedUuid, String playerCheckedName, UUID playerMatchedUuid, String playerMatchedName, AltAccountTrustScore altAccountTrustScore) {
        this.playerCheckedUuid = playerCheckedUuid;
        this.playerCheckedName = playerCheckedName;
        this.playerMatchedUuid = playerMatchedUuid;
        this.playerMatchedName = playerMatchedName;
        this.altAccountTrustScore = altAccountTrustScore;
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
    public AltAccountTrustScore getAltAccountTrustScore() {
        return altAccountTrustScore;
    }
}
