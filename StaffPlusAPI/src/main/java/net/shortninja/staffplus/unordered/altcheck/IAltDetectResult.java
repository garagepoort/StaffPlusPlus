package net.shortninja.staffplus.unordered.altcheck;

import java.util.UUID;

public interface IAltDetectResult {


    UUID getPlayerCheckedUuid();

    String getPlayerCheckedName();

    UUID getPlayerMatchedUuid();

    String getPlayerMatchedName();

    AltAccountTrustScore getAltAccountTrustScore();
}
