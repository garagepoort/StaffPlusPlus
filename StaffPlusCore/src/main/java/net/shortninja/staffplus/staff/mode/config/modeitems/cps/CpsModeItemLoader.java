package net.shortninja.staffplus.staff.mode.config.modeitems.cps;

import net.shortninja.staffplus.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class CpsModeItemLoader extends ModeItemLoader<CpsModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "cps-module";
    }

    @Override
    protected CpsModeConfiguration load(FileConfiguration config) {
        CpsModeConfiguration modeItemConfiguration = new CpsModeConfiguration(getModuleName(),
            config.getInt("staff-mode.cps-module.time") * 20,
            config.getInt("staff-mode.cps-module.max")
        );
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
