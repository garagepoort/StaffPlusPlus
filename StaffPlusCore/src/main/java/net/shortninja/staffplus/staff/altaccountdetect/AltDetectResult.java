package net.shortninja.staffplus.staff.altaccountdetect;

import net.shortninja.staffplus.unordered.altdetect.AltDetectTrustLevel;
import net.shortninja.staffplus.unordered.altdetect.IAltDetectResult;

import java.util.UUID;

public class AltDetectResult implements IAltDetectResult {

    private UUID playerCheckedUuid;
    private String playerCheckedName;
    private UUID playerMatchedUuid;
    private String playerMatchedName;
    private AltDetectTrustLevel altDetectTrustLevel;

    public AltDetectResult(UUID playerCheckedUuid, String playerCheckedName, UUID playerMatchedUuid, String playerMatchedName, AltDetectTrustLevel altDetectTrustLevel) {
        this.playerCheckedUuid = playerCheckedUuid;
        this.playerCheckedName = playerCheckedName;
        this.playerMatchedUuid = playerMatchedUuid;
        this.playerMatchedName = playerMatchedName;
        this.altDetectTrustLevel = altDetectTrustLevel;
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
}
