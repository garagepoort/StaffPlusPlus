package net.shortninja.staffplus.core.mode.config;

import be.garagepoort.mcioc.IocBean;

import java.util.Map;

@IocBean
public class StaffModeOptions {

    public Map<String, GeneralModeConfiguration> modeConfigurations;
    public final StaffItemsConfiguration staffItemsConfiguration;
    private final StaffModesLoader staffModesLoader;

    public StaffModeOptions(StaffModesLoader staffModesLoader,
                            StaffItemsConfiguration staffItemsConfiguration) {
        this.staffModesLoader = staffModesLoader;
        this.staffItemsConfiguration = staffItemsConfiguration;
        reload();
    }

    public void reload() {
        modeConfigurations = this.staffModesLoader.loadConfig();
    }

}