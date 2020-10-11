package net.shortninja.staffplus.unordered.altdetect;

import java.util.UUID;

public interface IAltDetectResult {


    UUID getPlayerCheckedUuid();

    String getPlayerCheckedName();

    UUID getPlayerMatchedUuid();

    String getPlayerMatchedName();

    AltAccountTrustScore getAltAccountTrustScore();
}
