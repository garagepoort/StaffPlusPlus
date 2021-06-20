package net.shortninja.staffplus.core.domain.staff.ban.config;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.domain.staff.ban.BanType;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BanConfiguration {

    private final boolean banEnabled;

    private final String commandBanPlayer;
    private final String commandTempBanPlayer;
    private final String commandUnbanPlayer;
    private final String commandManageBansGui;
    private final String permissionBanPlayer;
    private final String permissionTempbanPlayer;
    private final String permissionUnbanPlayer;
    private final String permissionBanByPass;
    private final String permissionBanTemplateOverwrite;
    private final String permissionBanView;
    private final String permissionBanSilent;
    private final GuiItemConfig guiItemConfig;
    private final String permBanTemplate;
    private final String tempBanTemplate;
    private final Map<String, String> templates;
    private final List<BanReasonConfiguration> banReasons;
    private final String staffNotificationPermission;

    public BanConfiguration(boolean banEnabled,
                            String commandBanPlayer,
                            String commandTempBanPlayer,
                            String commandUnbanPlayer,
                            String commandManageBansGui, String permissionBanPlayer,
                            String permissionTempbanPlayer, String permissionUnbanPlayer,
                            String permissionBanByPass,
                            String permissionBanTemplateOverwrite, String permissionBanView, String permissionBanSilent, GuiItemConfig guiItemConfig, String permBanTemplate,
                            String tempBanTemplate,
                            Map<String, String> templates, List<BanReasonConfiguration> banReasons, String staffNotificationPermission) {
        this.banEnabled = banEnabled;
        this.commandBanPlayer = commandBanPlayer;
        this.commandTempBanPlayer = commandTempBanPlayer;
        this.commandUnbanPlayer = commandUnbanPlayer;
        this.commandManageBansGui = commandManageBansGui;
        this.permissionBanPlayer = permissionBanPlayer;
        this.permissionTempbanPlayer = permissionTempbanPlayer;
        this.permissionUnbanPlayer = permissionUnbanPlayer;
        this.permissionBanByPass = permissionBanByPass;
        this.permissionBanTemplateOverwrite = permissionBanTemplateOverwrite;
        this.permissionBanView = permissionBanView;
        this.permissionBanSilent = permissionBanSilent;
        this.guiItemConfig = guiItemConfig;
        this.permBanTemplate = permBanTemplate;
        this.tempBanTemplate = tempBanTemplate;
        this.templates = templates;
        this.banReasons = banReasons;
        this.staffNotificationPermission = staffNotificationPermission;
    }

    public boolean isEnabled() {
        return banEnabled;
    }

    public GuiItemConfig getGuiItemConfig() {
        return guiItemConfig;
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
