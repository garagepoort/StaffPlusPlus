package net.shortninja.staffplus.staff.mode.config.modeitems.follow;

import net.shortninja.staffplus.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class FollowModeItemLoader extends ModeItemLoader<FollowModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "follow-module";
    }

    @Override
    protected FollowModeConfiguration load(FileConfiguration config) {
        FollowModeConfiguration modeItemConfiguration = new FollowModeConfiguration(getModuleName());
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
