package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import net.shortninja.staffplusplus.warnings.IWarning;

public class WarnMessageStringUtil {

    public static String replaceWarningPlaceholders(String message, IWarning warning) {
        String result = message;
        if (warning.getTargetName() != null) result = result.replace("%target%", warning.getTargetName());
        if (warning.getIssuerName() != null) result = result.replace("%issuer%", warning.getIssuerName());
        if (warning.getReason() != null) result = result.replace("%reason%", warning.getReason());
        if (warning.getSeverity() != null) result = result.replace("%severity%", warning.getSeverity());
        if (warning.getServerName() != null) result = result.replace("%server%", warning.getServerName());
        return result;
    }
}
