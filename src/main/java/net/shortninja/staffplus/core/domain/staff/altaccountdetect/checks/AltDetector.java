package net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks;

import net.shortninja.staffplus.core.domain.player.SppPlayer;

public interface AltDetector {

    int getScore(AltDetectInfo altDetectInfo, SppPlayer sppPlayer);
}
