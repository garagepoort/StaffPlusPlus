package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanTemplateLoader;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Optional;

@IocBean
public class IpBanConfiguration {

    @ConfigProperty("ban-module.ipban.enabled")
    public boolean banEnabled;

    @ConfigProperty("commands:commands.ipban.ban")
    public String commandIpBan;
    @ConfigProperty("commands:commands.ipban.tempban")
    public String commandIpTempBan;
    @ConfigProperty("commands:commands.ipban.unban")
    public String commandIpUnban;
    @ConfigProperty("commands:commands.ipban.bancheck")
    public String commandIpBanCheck;
    @ConfigProperty("commands:commands.ipban.ipbans")
    public String commandIpBans;

    @ConfigProperty("permissions:permissions.ipban.ban")
    public String permissionIpBan;
    @ConfigProperty("permissions:permissions.ipban.tempban")
    public String permissionIpTempBan;
    @ConfigProperty("permissions:permissions.ipban.unban")
    public String permissionUnban;
    @ConfigProperty("permissions:permissions.ipban.ban-view")
    public String permissionIpBanView;
    @ConfigProperty("permissions:permissions.ipban.ban-check")
    public String permissionIpBanCheck;
    @ConfigProperty("permissions:permissions.ipban.ban-silent")
    public String permissionBanSilent;
    @ConfigProperty("permissions:permissions.ipban.ban-template-overwrite")
    public String permissionBanTemplateOverwrite;
    @ConfigProperty("permissions:permissions.ipban.ban-notifications")
    public String staffNotificationPermission;

    @ConfigProperty("ban-module.ipban.permban-template")
    private String permBanTemplate;

    private final Map<String, String> templates;

    public IpBanConfiguration(BanTemplateLoader banTemplateLoader) {
        this.templates = banTemplateLoader.loadTemplates();
    }

    public Optional<String> getDefaultBanTemplate() {
        return StringUtils.isEmpty(permBanTemplate) ? Optional.empty() : Optional.ofNullable(permBanTemplate);
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public Optional<String> getTemplate(String template) {
        return Optional.ofNullable(templates.get(template));
    }

}
