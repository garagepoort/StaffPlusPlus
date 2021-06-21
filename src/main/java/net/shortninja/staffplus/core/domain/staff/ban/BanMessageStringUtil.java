package net.shortninja.staffplus.core.domain.staff.ban;

import net.shortninja.staffplusplus.ban.IBan;

public class BanMessageStringUtil {

    public static String replaceBanPlaceholders(String message, IBan ban) {
        String result = message;
        if (ban.getTargetName() != null) result = result.replace("%target%", ban.getTargetName());
        if (ban.getIssuerName() != null) result = result.replace("%issuer%", ban.getIssuerName());
        if (ban.getReason() != null) result = result.replace("%reason%", ban.getReason());
        if (ban.getEndTimestamp() != null) result = result.replace("%duration%", ban.getHumanReadableDuration());
        return result;
    }
}
