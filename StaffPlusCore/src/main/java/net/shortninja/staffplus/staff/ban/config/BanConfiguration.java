package net.shortninja.staffplus.staff.ban.config;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.common.exceptions.BusinessException;

import java.util.Map;

public class BanConfiguration {

    private final boolean banEnabled;

    private final String commandBanPlayer;
    private final String commandTempBanPlayer;
    private final String commandUnbanPlayer;
    private final String permissionBanPlayer;
    private final String permissionUnbanPlayer;
    private final String permissionBanByPass;
    private final GuiItemConfig guiItemConfig;
    private final String permBanTemplate;
    private final String tempBanTemplate;
    private final Map<String, String> templates;

    public BanConfiguration(boolean banEnabled,
                            String commandBanPlayer,
                            String commandTempBanPlayer,
                            String commandUnbanPlayer,
                            String permissionBanPlayer,
                            String permissionUnbanPlayer,
                            String permissionBanByPass,
                            GuiItemConfig guiItemConfig, String permBanTemplate,
                            String tempBanTemplate,
                            Map<String, String> templates) {
        this.banEnabled = banEnabled;
        this.commandBanPlayer = commandBanPlayer;
        this.commandTempBanPlayer = commandTempBanPlayer;
        this.commandUnbanPlayer = commandUnbanPlayer;
        this.permissionBanPlayer = permissionBanPlayer;
        this.permissionUnbanPlayer = permissionUnbanPlayer;
        this.permissionBanByPass = permissionBanByPass;
        this.guiItemConfig = guiItemConfig;
        this.permBanTemplate = permBanTemplate;
        this.tempBanTemplate = tempBanTemplate;
        this.templates = templates;
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

    public String getPermissionBanPlayer() {
        return permissionBanPlayer;
    }

    public String getPermissionBanByPass() {
        return permissionBanByPass;
    }

    public String getPermissionUnbanPlayer() {
        return permissionUnbanPlayer;
    }

    public String getPermBanTemplate(String template) {
        if (template != null) {
            return getTemplate(template);
        }

        if (permBanTemplate != null) {
            return getTemplate(permBanTemplate);
        }

        return IocContainer.getMessages().permanentBannedKick;
    }

    public String getTempBanTemplate(String template) {
        if (template != null) {
            return getTemplate(template);
        }

        if (tempBanTemplate != null) {
            return getTemplate(tempBanTemplate);
        }

        return IocContainer.getMessages().tempBannedKick;
    }

    private String getTemplate(String template) {
        if (!templates.containsKey(template)) {
            throw new BusinessException("&CCannot find ban template with name [" + template + "]");
        }
        return templates.get(template);
    }

    public Map<String, String> getTemplates() {
        return templates;
    }
}
