package net.shortninja.staffplus.staff.altaccountdetect.checks;

import net.shortninja.staffplus.player.SppPlayer;

public interface AltDetector {

    int getScore(AltDetectInfo altDetectInfo, SppPlayer sppPlayer);
}
