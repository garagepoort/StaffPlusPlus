package net.shortninja.staffplus.core.domain.staff.ban;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanReasonConfiguration;

import java.util.Optional;

@IocBean
public class BanTemplateResolver {

    private final Options options;
    private final Messages messages;

    public BanTemplateResolver(Options options, Messages messages) {
        this.options = options;
        this.messages = messages;
    }

    public String resolveTemplate(String reason, String providedTemplate, BanType banType) {
        if (providedTemplate != null) {
            return getTemplate(providedTemplate);
        }

        Optional<BanReasonConfiguration> banReasonConfig = options.banConfiguration.getBanReason(reason, banType);
        if (banReasonConfig.isPresent() && banReasonConfig.get().getTemplate().isPresent()) {
            return getTemplate(banReasonConfig.get().getTemplate().get());
        }

        Optional<String> defaultTemplate = options.banConfiguration.getDefaultBanTemplate(banType);
        if (defaultTemplate.isPresent()) {
            return getTemplate(defaultTemplate.get());
        }

        return banType == BanType.PERM_BAN ? messages.permanentBannedKick : messages.tempBannedKick;
    }

    private String getTemplate(String providedTemplate) {
        return options.banConfiguration.getTemplate(providedTemplate)
            .orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + providedTemplate + "]"));
    }
}
