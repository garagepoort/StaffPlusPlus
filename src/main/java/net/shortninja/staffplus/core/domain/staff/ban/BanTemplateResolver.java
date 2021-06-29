package net.shortninja.staffplus.core.domain.staff.ban;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanReasonConfiguration;

import java.util.Optional;

@IocBean
public class BanTemplateResolver {

    private final BanConfiguration banConfiguration;
    private final Messages messages;

    public BanTemplateResolver(BanConfiguration banConfiguration, Messages messages) {
        this.banConfiguration = banConfiguration;
        this.messages = messages;
    }

    public String resolveTemplate(String reason, String providedTemplate, BanType banType) {
        if (providedTemplate != null) {
            return getTemplate(providedTemplate);
        }

        Optional<BanReasonConfiguration> banReasonConfig = banConfiguration.getBanReason(reason, banType);
        if (banReasonConfig.isPresent() && banReasonConfig.get().getTemplate().isPresent()) {
            return getTemplate(banReasonConfig.get().getTemplate().get());
        }

        Optional<String> defaultTemplate = banConfiguration.getDefaultBanTemplate(banType);
        if (defaultTemplate.isPresent()) {
            return getTemplate(defaultTemplate.get());
        }

        return banType == BanType.PERM_BAN ? messages.permanentBannedKick : messages.tempBannedKick;
    }

    private String getTemplate(String providedTemplate) {
        return banConfiguration.getTemplate(providedTemplate)
            .orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + providedTemplate + "]"));
    }
}
