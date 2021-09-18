package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanReasonConfiguration;

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
        Optional<String> templateName = getTemplateName(reason, providedTemplate, banType);
        if (templateName.isPresent()) {
            return getTemplate(templateName.get());
        }

        return banType == BanType.PERM_BAN ? messages.permanentBannedKick : messages.tempBannedKick;
    }

    public Optional<String> getTemplateName(String reason, String providedTemplate, BanType banType) {
        if (providedTemplate != null) {
            return Optional.of(providedTemplate);
        }

        Optional<BanReasonConfiguration> banReasonConfig = banConfiguration.getBanReason(reason, banType);
        if (banReasonConfig.isPresent() && banReasonConfig.get().getTemplate().isPresent()) {
            return Optional.of(banReasonConfig.get().getTemplate().get());
        }

        return banConfiguration.getDefaultBanTemplate(banType);
    }

    public boolean hasTemplate(String providedTemplate) {
        return banConfiguration.getTemplate(providedTemplate).isPresent();
    }

    private String getTemplate(String providedTemplate) {
        return banConfiguration.getTemplate(providedTemplate)
            .orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + providedTemplate + "]"));
    }
}
