package net.shortninja.staffplus.staff.altaccountdetect.checks;

import net.shortninja.staffplus.player.SppPlayer;
import org.apache.commons.lang.StringUtils;

public class UsernameDetector implements AltDetector {
    @Override
    public int getScore(AltDetectInfo altDetectInfo, SppPlayer sppPlayer) {
        String strippedUsernamePlayer = stripUsername(altDetectInfo.getUsername());
        String strippedUsernameOtherPlayer = stripUsername(sppPlayer.getUsername());
        int levenshteinDistance = StringUtils.getLevenshteinDistance(strippedUsernamePlayer, strippedUsernameOtherPlayer);
        double changePercentage = ((double) levenshteinDistance) / (Math.max(strippedUsernamePlayer.length(), strippedUsernameOtherPlayer.length()));
        if (changePercentage <= 0.3) {
            return 3;
        }
        if (changePercentage <= 0.5) {
            return 2;
        }
        if (changePercentage <= 0.7) {
            return 1;
        }
        return 0;
    }

    private String stripUsername(String username) {
        return username.toLowerCase().trim().replace("-", "").replace("_", "");
    }

}
