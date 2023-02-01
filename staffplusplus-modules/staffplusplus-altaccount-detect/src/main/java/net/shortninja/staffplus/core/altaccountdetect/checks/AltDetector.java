package net.shortninja.staffplus.core.altaccountdetect.checks;

import net.shortninja.staffplusplus.altdetect.AltDetectResultType;
import net.shortninja.staffplusplus.session.SppPlayer;

public interface AltDetector {

    AltDetectResultType getResult(AltDetectInfo altDetectInfo, SppPlayer sppPlayer);
}
