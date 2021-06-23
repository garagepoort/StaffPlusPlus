package net.shortninja.staffplus.core.domain.staff.ban;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanReasonConfiguration;

import java.util.List;

@IocBean
public class BanReasonResolver {

    private final BanConfiguration banConfiguration;

    public BanReasonResolver(BanConfiguration banConfiguration) {
        this.banConfiguration = banConfiguration;
    }

    public String resolveBanReason(String reason, BanType banType) {
        List<BanReasonConfiguration> banReasons = banConfiguration.getBanReasons(banType);
        if (banReasons.isEmpty()) {
            return reason;
        }
        return banConfiguration.getBanReason(reason, banType)
            .map(BanReasonConfiguration::getReason)
            .orElseThrow(() -> new BusinessException("&CNo ban reason config found for name: [" + reason + "]"));
    }
}
