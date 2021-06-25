package net.shortninja.staffplus.core.domain.staff.ban.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.common.gui.IGuiItemConfig;
import net.shortninja.staffplus.core.domain.staff.ban.BanType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean
public class BanConfiguration {

    @ConfigProperty("ban-module.enabled")
    private boolean banEnabled;

    @ConfigProperty("commands:commands.ban")
    private String commandBanPlayer;
    @ConfigProperty("commands:commands.tempban")
    private String commandTempBanPlayer;
    @ConfigProperty("commands:commands.unban")
    private String commandUnbanPlayer;
    @ConfigProperty("commands:commands.bans.manage.gui")
    private String commandManageBansGui;

    @ConfigProperty("permissions:permissions.ban")
    private String permissionBanPlayer;
    @ConfigProperty("permissions:permissions.tempban")
    private String permissionTempbanPlayer;
    @ConfigProperty("permissions:permissions.unban")
    private String permissionUnbanPlayer;
    @ConfigProperty("permissions:permissions.ban-bypass")
    private String permissionBanByPass;
    @ConfigProperty("permissions:permissions.ban-template-overwrite")
    private String permissionBanTemplateOverwrite;
    @ConfigProperty("permissions:permissions.ban-view")
    private String permissionBanView;
    @ConfigProperty("permissions:permissions.ban-silent")
    private String permissionBanSilent;
    @ConfigProperty("permissions:permissions.ban-notifications")
    private String staffNotificationPermission;

    @ConfigProperty("ban-module.permban-template")
    private String permBanTemplate;
    @ConfigProperty("ban-module.tempban-template")
    private String tempBanTemplate;

    @ConfigProperty("ban-module.reasons")
    @ConfigTransformer(BanReasonConfigMapper.class)
    private List<BanReasonConfiguration> banReasons = new ArrayList<>();

    private final Map<String, String> templates;
    private final BanGuiItemConfig banGuiItemConfig;

    public BanConfiguration(BanTemplateLoader banTemplateLoader, BanGuiItemConfig banGuiItemConfig) {
        this.templates = banTemplateLoader.loadTemplates();
        this.banGuiItemConfig = banGuiItemConfig;
    }

    public boolean isEnabled() {
        return banEnabled;
    }

    public IGuiItemConfig getGuiItemConfig() {
        return banGuiItemConfig;
    }

    public String getCommandBanPlayer() {
        return commandBanPlayer;
    }

    public String getCommandUnbanPlayer() {
        return commandUnbanPlayer;
    }

    public String getCommandTempBanPlayer() {
        return commandTempBanPlayer;
    }

    public String getCommandManageBansGui() {
        return commandManageBansGui;
    }

    public String getPermissionBanPlayer() {
        return permissionBanPlayer;
    }

    public String getPermissionTempbanPlayer() {
        return permissionTempbanPlayer;
    }

    public String getPermissionBanByPass() {
        return permissionBanByPass;
    }

    public String getPermissionUnbanPlayer() {
        return permissionUnbanPlayer;
    }

    public String getPermissionBanTemplateOverwrite() {
        return permissionBanTemplateOverwrite;
    }

    public Optional<String> getDefaultBanTemplate(BanType banType) {
        if (banType == BanType.PERM_BAN) {
            return StringUtils.isEmpty(permBanTemplate) ? Optional.empty() : Optional.ofNullable(permBanTemplate);
        }
        return StringUtils.isEmpty(tempBanTemplate) ? Optional.empty() : Optional.ofNullable(tempBanTemplate);
    }

    public String getPermissionBanView() {
        return permissionBanView;
    }

    public Optional<String> getTemplate(String template) {
        return Optional.ofNullable(templates.get(template));
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public Optional<BanReasonConfiguration> getBanReason(String reason, BanType banType) {
        return getBanReasons(banType).stream().filter(b -> b.getName().equalsIgnoreCase(reason)).findFirst();
    }

    public List<BanReasonConfiguration> getBanReasons(BanType banType) {
        return banReasons.stream()
            .filter(b -> !b.getBanType().isPresent() || b.getBanType().get() == banType)
            .collect(Collectors.toList());
    }

    public String getStaffNotificationPermission() {
        return staffNotificationPermission;
    }

    public String getPermissionBanSilent() {
        return permissionBanSilent;
    }
}
