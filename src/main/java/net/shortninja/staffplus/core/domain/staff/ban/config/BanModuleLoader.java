package net.shortninja.staffplus.core.domain.staff.ban.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.domain.staff.ban.BanType;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
public class BanModuleLoader extends AbstractConfigLoader<BanConfiguration> {

    @Override
    protected BanConfiguration load() {
        boolean banEnabled = defaultConfig.getBoolean("ban-module.enabled");
        String permBanTemplate = defaultConfig.getString("ban-module.permban-template");
        String tempBanTemplate = defaultConfig.getString("ban-module.tempban-template");

        boolean modeGuiBan = staffModeModulesConfig.getBoolean("modules.gui-module.ban-gui");
        String modeGuiBanTitle = staffModeModulesConfig.getString("modules.gui-module.ban-title");
        String modeGuiBanName = staffModeModulesConfig.getString("modules.gui-module.ban-name");
        String modeGuiBanLore = staffModeModulesConfig.getString("modules.gui-module.ban-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiBan, modeGuiBanTitle, modeGuiBanName, modeGuiBanLore);

        String commandBanPlayer = commandsConfig.getString("commands.ban");
        String commandUnbanPlayer = commandsConfig.getString("commands.unban");
        String commandTempBanPlayer = commandsConfig.getString("commands.tempban");

        String permissionBanPlayer = permissionsConfig.getString("permissions.ban");
        String permissionUnbanPlayer = permissionsConfig.getString("permissions.unban");
        String permissionBanByPass = permissionsConfig.getString("permissions.ban-bypass");
        String permissionBanTemplateOverwrite = permissionsConfig.getString("permissions.ban-template-overwrite");

        return new BanConfiguration(banEnabled,
            commandBanPlayer,
            commandTempBanPlayer,
            commandUnbanPlayer,
            permissionBanPlayer,
            permissionUnbanPlayer,
            permissionBanByPass,
            permissionBanTemplateOverwrite,
            guiItemConfig,
            permBanTemplate,
            tempBanTemplate,
            getTemplates(),
            getBanReasons(defaultConfig));
    }

    private Map<String, String> getTemplates() {
        String directoryPath = StaffPlus.get().getDataFolder() + File.separator + "bans";
        File banDir = new File(directoryPath);
        if (!banDir.exists()) {
            return Collections.emptyMap();
        }
        Map<String, String> templates = new HashMap<>();
        for (File file : Objects.requireNonNull(banDir.listFiles())) {
            templates.put(getFileNameWithoutExtension(file), readTemplate(file));
        }
        return templates;
    }

    private String getFileNameWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    private String readTemplate(File filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(filePath.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
        return contentBuilder.toString();
    }

    private List<BanReasonConfiguration> getBanReasons(FileConfiguration config) {
        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) config.getList("ban-module.reasons", new ArrayList<>());

        return Objects.requireNonNull(list).stream().map(map -> {
            String name = (String) map.get("name");
            String reason = (String) map.get("reason");
            String template = (String) map.get("template");
            BanType banType = map.containsKey("ban-type") ? BanType.valueOf((String) map.get("ban-type")) : null;
            return new BanReasonConfiguration(name, reason, template, banType);
        }).collect(Collectors.toList());
    }
}
