package net.shortninja.staffplus.core.domain.staff.ban.playerbans.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean
public class BanConfiguration {

    @ConfigProperty("ban-module.enabled")
    public boolean enabled;

    @ConfigProperty("commands:commands.tempban")
    public String commandTempBanPlayer;
    @ConfigProperty("commands:commands.unban")
    public String commandUnbanPlayer;
    @ConfigProperty("commands:commands.bans.manage.gui")
    public String commandManageBansGui;

    @ConfigProperty("permissions:permissions.tempban")
    public String permissionTempbanPlayer;
    @ConfigProperty("permissions:permissions.unban")
    public String permissionUnbanPlayer;
    @ConfigProperty("permissions:permissions.ban-bypass")
    public String permissionBanByPass;
    @ConfigProperty("permissions:permissions.ban-template-overwrite")
    public String permissionBanTemplateOverwrite;
    @ConfigProperty("permissions:permissions.ban-view")
    public String permissionBanView;
    @ConfigProperty("permissions:permissions.ban-silent")
    public String permissionBanSilent;
    @ConfigProperty("permissions:permissions.ban-notifications")
    public String staffNotificationPermission;

    @ConfigProperty("ban-module.permban-template")
    public String permBanTemplate;
    @ConfigProperty("ban-module.tempban-template")
    public String tempBanTemplate;

    @ConfigProperty("ban-module.reasons")
    @ConfigTransformer(BanReasonConfigMapper.class)
    public List<BanReasonConfiguration> banReasons = new ArrayList<>();

    public final Map<String, String> templates;
    public final BanGuiItemConfig banGuiItemConfig;

    public BanConfiguration(BanTemplateLoader banTemplateLoader, BanGuiItemConfig banGuiItemConfig) {
        this.templates = banTemplateLoader.loadTemplates();
        this.banGuiItemConfig = banGuiItemConfig;
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

    public Optional<BanReasonConfiguration> getBanReason(String reason, BanType banType) {
        return getBanReasons(banType).stream().filter(b -> b.getName().equalsIgnoreCase(reason)).findFirst();
    }

    public List<BanReasonConfiguration> getBanReasons(BanType banType) {
        return banReasons.stream()
            .filter(b -> !b.getBanType().isPresent() || b.getBanType().get() == banType)
            .collect(Collectors.toList());
    }

}
