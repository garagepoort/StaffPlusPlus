package net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks;

import net.shortninja.staffplusplus.altdetect.AltDetectResultType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;

import static net.shortninja.staffplusplus.altdetect.AltDetectResultType.NAME_SIMILARITY_30;
import static net.shortninja.staffplusplus.altdetect.AltDetectResultType.NAME_SIMILARITY_50;
import static net.shortninja.staffplusplus.altdetect.AltDetectResultType.NAME_SIMILARITY_70;

public class UsernameDetector implements AltDetector {
    @Override
    public AltDetectResultType getResult(AltDetectInfo altDetectInfo, SppPlayer sppPlayer) {
        String strippedUsernamePlayer = stripUsername(altDetectInfo.getUsername());
        String strippedUsernameOtherPlayer = stripUsername(sppPlayer.getUsername());
        int levenshteinDistance = StringUtils.getLevenshteinDistance(strippedUsernamePlayer, strippedUsernameOtherPlayer);
        double changePercentage = ((double) levenshteinDistance) / (Math.max(strippedUsernamePlayer.length(), strippedUsernameOtherPlayer.length()));
        if (changePercentage <= 0.3) {
            return NAME_SIMILARITY_70;
        }
        if (changePercentage <= 0.5) {
            return NAME_SIMILARITY_50;
        }
        if (changePercentage <= 0.7) {
            return NAME_SIMILARITY_30;
        }
        return null;
    }

    private String stripUsername(String username) {
        return username.toLowerCase().trim().replace("-", "").replace("_", "");
    }

}
