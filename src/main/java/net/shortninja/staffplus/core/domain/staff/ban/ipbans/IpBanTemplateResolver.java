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
        Optional<String> template = getTemplate(providedTemplate, banType);
        if (template.isPresent()) {
            return getTemplate(template.get());
        }

        return banType == BanType.PERM_BAN ? messages.ipbanPermabannedKick : messages.ipbanTempbannedKick;
    }

    public Optional<String> getTemplate(String providedTemplate, BanType banType) {
        if (providedTemplate != null) {
            return Optional.of(providedTemplate);
        }

        return banConfiguration.getDefaultBanTemplate(banType);
    }

    private String getTemplate(String providedTemplate) {
        return banConfiguration.getTemplate(providedTemplate)
            .orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + providedTemplate + "]"));
    }

    public boolean hasTemplate(String s) {
        return banConfiguration.getTemplate(s).isPresent();
    }
}
