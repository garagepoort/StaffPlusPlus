package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanTemplateLoader;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Optional;

@IocBean
public class IpBanConfiguration {

    @ConfigProperty("permissions:ipban.ban-silent")
    public String permissionBanSilent;
    @ConfigProperty("permissions:ipban.ban-template-overwrite")
    public String permissionBanTemplateOverwrite;
    @ConfigProperty("permissions:ipban.ban-notifications")
    public String staffNotificationPermission;

    @ConfigProperty("ban-module.ipban.permban-template")
    private String permBanTemplate;
    @ConfigProperty("ban-module.ipban.tempban-template")
    private String tempBanTemplate;

    private final Map<String, String> templates;

    public IpBanConfiguration(BanTemplateLoader banTemplateLoader) {
        this.templates = banTemplateLoader.loadTemplates();
    }

    public Optional<String> getDefaultBanTemplate(BanType banType) {
        if (banType == BanType.PERM_BAN) {
            return StringUtils.isEmpty(permBanTemplate) ? Optional.empty() : Optional.of(permBanTemplate);
        }
        return StringUtils.isEmpty(tempBanTemplate) ? Optional.empty() : Optional.of(tempBanTemplate);
    }

    public Optional<String> getTemplate(String template) {
        return Optional.ofNullable(templates.get(template));
    }

}
