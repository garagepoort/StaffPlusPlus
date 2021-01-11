package net.shortninja.staffplus.staff.mode.config.modeitems.gui;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;

public class GuiModeConfiguration extends ModeItemConfiguration {
    public boolean modeGuiMiner;
    public String modeGuiMinerTitle;
    public String modeGuiMinerName;
    public String modeGuiMinerLore;
    public int modeGuiMinerLevel;

    public GuiModeConfiguration(String identifier, boolean modeGuiMiner, String modeGuiMinerTitle, String modeGuiMinerName, String modeGuiMinerLore, int modeGuiMinerLevel) {
        super(identifier);
        this.modeGuiMiner = modeGuiMiner;
        this.modeGuiMinerTitle = modeGuiMinerTitle;
        this.modeGuiMinerName = modeGuiMinerName;
        this.modeGuiMinerLore = modeGuiMinerLore;
        this.modeGuiMinerLevel = modeGuiMinerLevel;
    }
}
