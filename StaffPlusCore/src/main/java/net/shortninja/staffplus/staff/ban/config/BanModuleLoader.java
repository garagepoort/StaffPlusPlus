package net.shortninja.staffplus.staff.ban.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class BanModuleLoader extends ConfigLoader<BanConfiguration> {

    @Override
    protected BanConfiguration load(FileConfiguration config) {
        boolean banEnabled = config.getBoolean("ban-module.enabled");
        String permBanTemplate = config.getString("ban-module.permban-template");
        String tempBanTemplate = config.getString("ban-module.tempban-template");

        boolean modeGuiBan = config.getBoolean("staff-mode.gui-module.ban-gui");
        String modeGuiBanTitle = config.getString("staff-mode.gui-module.ban-title");
        String modeGuiBanName = config.getString("staff-mode.gui-module.ban-name");
        String modeGuiBanLore = config.getString("staff-mode.gui-module.ban-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiBan, modeGuiBanTitle, modeGuiBanName, modeGuiBanLore);

        String commandBanPlayer = config.getString("commands.ban");
        String commandUnbanPlayer = config.getString("commands.unban");
        String commandTempBanPlayer = config.getString("commands.tempban");

        String permissionBanPlayer = config.getString("permissions.ban");
        String permissionUnbanPlayer = config.getString("permissions.unban");
        String permissionBanByPass = config.getString("permissions.ban-bypass");

        return new BanConfiguration(banEnabled, commandBanPlayer, commandTempBanPlayer, commandUnbanPlayer, permissionBanPlayer,
            permissionUnbanPlayer, permissionBanByPass, guiItemConfig,
            permBanTemplate,
            tempBanTemplate,
            getTemplates());
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
            throw new RuntimeException(e);
        }
        return contentBuilder.toString();
    }
}
