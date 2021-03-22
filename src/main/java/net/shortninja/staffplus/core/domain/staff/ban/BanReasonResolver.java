package net.shortninja.staffplus.core.domain.staff.ban;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanReasonConfiguration;

import java.util.List;

@IocBean
public class BanReasonResolver {

    private final Options options;

    public BanReasonResolver(Options options) {
        this.options = options;
    }

    public String resolveBanReason(String reason, BanType banType) {
        List<BanReasonConfiguration> banReasons = options.banConfiguration.getBanReasons(banType);
        if (banReasons.isEmpty()) {
            return reason;
        }
        return options.banConfiguration.getBanReason(reason, banType)
            .map(BanReasonConfiguration::getReason)
            .orElseThrow(() -> new BusinessException("&CNo ban reason config found for name: [" + reason + "]"));
    }
}
