package net.shortninja.staffplus.core.domain.staff.mute;

import net.shortninja.staffplusplus.mute.IMute;

public class MuteMessageStringUtil {

    public static String replaceMutePlaceholders(String message, IMute mute) {
        String result = message;
        if (mute.getTargetName() != null) result = result.replace("%target%", mute.getTargetName());
        if (mute.getIssuerName() != null) result = result.replace("%issuer%", mute.getIssuerName());
        if (mute.getReason() != null) result = result.replace("%reason%", mute.getReason());
        if (mute.getEndTimestamp() != null) result = result.replace("%duration%", mute.getHumanReadableDuration());
        return result;
    }
}
