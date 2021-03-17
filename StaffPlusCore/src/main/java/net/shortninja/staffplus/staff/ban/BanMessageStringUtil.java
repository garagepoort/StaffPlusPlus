package net.shortninja.staffplus.staff.ban;

import net.shortninja.staffplus.common.JavaUtils;

public class BanMessageStringUtil {

    public static String replaceBanPlaceholders(String message, String target, String issuerName, String reason, Long duration) {
        String result = message;
        if (target != null) result = result.replace("%target%", target);
        if (issuerName != null) result = result.replace("%issuer%", issuerName);
        if (reason != null) result = result.replace("%reason%", reason);
        if (duration != null) result = result.replace("%duration%", JavaUtils.toHumanReadableDuration(duration));
        return result;
    }
}
