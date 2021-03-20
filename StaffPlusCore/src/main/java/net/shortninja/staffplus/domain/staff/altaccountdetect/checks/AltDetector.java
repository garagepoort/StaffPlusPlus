package net.shortninja.staffplus.domain.staff.altaccountdetect.checks;

import net.shortninja.staffplus.domain.player.SppPlayer;

public interface AltDetector {

    int getScore(AltDetectInfo altDetectInfo, SppPlayer sppPlayer);
}
