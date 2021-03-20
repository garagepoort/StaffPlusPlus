package net.shortninja.staffplus.domain.staff.mode.config.modeitems.examine;

import net.shortninja.staffplus.domain.staff.mode.config.ModeItemConfiguration;

public class ExamineModeConfiguration extends ModeItemConfiguration {

    private String modeExamineTitle;
    private int modeExamineFood;
    private int modeExamineIp;
    private int modeExamineGamemode;
    private int modeExamineInfractions;
    private int modeExamineLocation;
    private int modeExamineNotes;
    private int modeExamineFreeze;
    private int modeExamineWarn;

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
