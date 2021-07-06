package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import net.shortninja.staffplusplus.ban.IIpBan;

public class IpBanMessageStringUtil {

    public static String replaceBanPlaceholders(String message, IIpBan ban) {
        String result = message;
        if (ban.getIssuerName() != null) result = result.replace("%issuer%", ban.getIssuerName());
        if (ban.getIp() != null) result = result.replace("%ip%", ban.getIp());
        if (ban.getEndTimestamp().isPresent()) result = result.replace("%duration%", ban.getHumanReadableDuration());
        return result;
    }
}
