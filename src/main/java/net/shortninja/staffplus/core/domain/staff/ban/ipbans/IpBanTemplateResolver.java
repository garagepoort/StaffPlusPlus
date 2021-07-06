package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;

import java.util.Optional;

@IocBean
public class IpBanTemplateResolver {

    private final IpBanConfiguration banConfiguration;
    private final Messages messages;

    public IpBanTemplateResolver(IpBanConfiguration banConfiguration, Messages messages) {
        this.banConfiguration = banConfiguration;
        this.messages = messages;
    }

    public String resolveTemplate(String providedTemplate, BanType banType) {
        if (providedTemplate != null) {
            return getTemplate(providedTemplate);
        }

        Optional<String> defaultTemplate = banConfiguration.getDefaultBanTemplate(banType);
        if (defaultTemplate.isPresent()) {
            return getTemplate(defaultTemplate.get());
        }

        return banType == BanType.PERM_BAN ? messages.ipbanPermabannedKick : messages.ipbanTempbannedKick;
    }

    private String getTemplate(String providedTemplate) {
        return banConfiguration.getTemplate(providedTemplate)
            .orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + providedTemplate + "]"));
    }
}
