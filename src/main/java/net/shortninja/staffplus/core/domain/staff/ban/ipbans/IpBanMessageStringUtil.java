package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto.IpBanBungeeDto;
import net.shortninja.staffplusplus.ban.IIpBan;

public class IpBanMessageStringUtil {

    public static String replaceBanPlaceholders(String message, IIpBan ban) {
        String result = message;
        if (ban.getIssuerName() != null) result = result.replace("%issuer%", ban.getIssuerName());
        if (ban.getIp() != null) result = result.replace("%ip%", ban.getIp());
        if (ban.getEndTimestamp().isPresent()) result = result.replace("%duration%", ban.getHumanReadableDuration());
        return result;
    }

    public static String replaceBanPlaceholders(String message, IpBanBungeeDto ban) {
        String result = message;
        if (ban.getIssuerName() != null) result = result.replace("%issuer%", ban.getIssuerName());
        if (ban.getIp() != null) result = result.replace("%ip%", ban.getIp());
        if (ban.getEndTimestamp() != null) result = result.replace("%duration%", ban.getHumanReadableDuration());
        return result;
    }
}
