package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.dto.BanBungeeDto;
import net.shortninja.staffplusplus.ban.IBan;

public class BanMessageStringUtil {

    public static String replaceBanPlaceholders(String message, IBan ban) {
        return replace(message, ban.getTargetName(), ban.getIssuerName(), ban.getReason(), ban.getEndTimestamp(), ban.getHumanReadableDuration());
    }

    public static String replaceBanPlaceholders(String message, BanBungeeDto ban) {
        return replace(message, ban.getTargetName(), ban.getIssuerName(), ban.getReason(), ban.getEndTimestamp(), ban.getHumanReadableDuration());
    }

    public static String replace(String message, String targetName, String issuerName, String reason, Long endTimestamp, String humanReadableDuration) {
        String result = message;
        if (targetName != null) result = result.replace("%target%", targetName);
        if (issuerName != null) result = result.replace("%issuer%", issuerName);
        if (reason != null) result = result.replace("%reason%", reason);
        if (endTimestamp != null) result = result.replace("%duration%", humanReadableDuration);
        return result;
    }
}
