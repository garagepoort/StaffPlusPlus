package net.shortninja.staffplus.staff.mode.config.modeitems.counter;

import net.shortninja.staffplus.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class CounterModeItemLoader extends ModeItemLoader<CounterModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "counter-module";
    }

    @Override
    protected CounterModeConfiguration load(FileConfiguration config) {
        CounterModeConfiguration modeItemConfiguration = new CounterModeConfiguration(
            config.getBoolean("staff-mode.counter-module.show-staff-mode"),
            config.getString("staff-mode.counter-module.title"));
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
