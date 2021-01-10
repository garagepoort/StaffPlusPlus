package net.shortninja.staffplus.staff.mode.config.modeitems.examine;

import net.shortninja.staffplus.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class ExamineModeItemLoader extends ModeItemLoader<ExamineModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "examine-module";
    }

    @Override
    protected ExamineModeConfiguration load(FileConfiguration config) {
        ExamineModeConfiguration modeItemConfiguration = new ExamineModeConfiguration(
            config.getString("staff-mode.examine-module.title"),
            config.getInt("staff-mode.examine-module.info-line.food") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.food"),
            config.getInt("staff-mode.examine-module.info-line.ip-address") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.ip-address"),
            config.getInt("staff-mode.examine-module.info-line.gamemode") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.gamemode"),
            config.getInt("staff-mode.examine-module.info-line.infractions") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.infractions"),
            config.getInt("staff-mode.examine-module.info-line.location") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.location"),
            config.getInt("staff-mode.examine-module.info-line.notes") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.notes"),
            config.getInt("staff-mode.examine-module.info-line.freeze") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.freeze"),
            config.getInt("staff-mode.examine-module.info-line.warn") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.warn"));
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
