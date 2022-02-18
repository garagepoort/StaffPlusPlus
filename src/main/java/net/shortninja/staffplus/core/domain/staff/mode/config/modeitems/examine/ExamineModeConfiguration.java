package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine;

import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;

public class ExamineModeConfiguration extends ModeItemConfiguration {

    private final String modeExamineTitle;
    private final int modeExamineFood;
    private final int modeExamineIp;
    private final int modeExamineGamemode;
    private final int modeExamineInfractions;
    private final int modeExamineLocation;
    private final int modeExamineNotes;
    private final int modeExamineFreeze;
    private final int modeExamineWarn;

    public ExamineModeConfiguration(String identifier, String modeExamineTitle, int modeExamineFood, int modeExamineIp, int modeExamineGamemode, int modeExamineInfractions, int modeExamineLocation, int modeExamineNotes, int modeExamineFreeze, int modeExamineWarn) {
        super(identifier);
        this.modeExamineTitle = modeExamineTitle;
        this.modeExamineFood = modeExamineFood;
        this.modeExamineIp = modeExamineIp;
        this.modeExamineGamemode = modeExamineGamemode;
        this.modeExamineInfractions = modeExamineInfractions;
        this.modeExamineLocation = modeExamineLocation;
        this.modeExamineNotes = modeExamineNotes;
        this.modeExamineFreeze = modeExamineFreeze;
        this.modeExamineWarn = modeExamineWarn;
    }

    public String getModeExamineTitle() {
        return modeExamineTitle;
    }

    public int getModeExamineFood() {
        return modeExamineFood;
    }

    public int getModeExamineIp() {
        return modeExamineIp;
    }

    public int getModeExamineGamemode() {
        return modeExamineGamemode;
    }

    public int getModeExamineInfractions() {
        return modeExamineInfractions;
    }

    public int getModeExamineLocation() {
        return modeExamineLocation;
    }

    public int getModeExamineNotes() {
        return modeExamineNotes;
    }

    public int getModeExamineFreeze() {
        return modeExamineFreeze;
    }

    public int getModeExamineWarn() {
        return modeExamineWarn;
    }
}
