package net.shortninja.staffplus.domain.staff.mode.config.modeitems.freeze;

import net.shortninja.staffplus.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.common.Sounds;

import java.util.List;

public class FreezeModeConfiguration extends ModeItemConfiguration {

    private int modeFreezeTimer;
    private Sounds modeFreezeSound;
    private boolean modeFreezePrompt;
    private String modeFreezePromptTitle;
    private boolean modeFreezeChat;
    private boolean modeFreezeDamage;
    private boolean titleMessageEnabled;
    private final List<String> logoutCommands;

    public FreezeModeConfiguration(String identifier, int modeFreezeTimer, Sounds modeFreezeSound, boolean modeFreezePrompt, String modeFreezePromptTitle, boolean modeFreezeChat, boolean modeFreezeDamage, boolean titleMessageEnabled, List<String> logoutCommands) {
        super(identifier);
        this.modeFreezeTimer = modeFreezeTimer;
        this.modeFreezeSound = modeFreezeSound;
        this.modeFreezePrompt = modeFreezePrompt;
        this.modeFreezePromptTitle = modeFreezePromptTitle;
        this.modeFreezeChat = modeFreezeChat;
        this.modeFreezeDamage = modeFreezeDamage;
        this.titleMessageEnabled = titleMessageEnabled;
        this.logoutCommands = logoutCommands;
    }

    public int getModeFreezeTimer() {
        return modeFreezeTimer;
    }

    public Sounds getModeFreezeSound() {
        return modeFreezeSound;
    }

    public boolean isModeFreezePrompt() {
        return modeFreezePrompt;
    }

    public String getModeFreezePromptTitle() {
        return modeFreezePromptTitle;
    }

    public boolean isModeFreezeChat() {
        return modeFreezeChat;
    }

    public boolean isModeFreezeDamage() {
        return modeFreezeDamage;
    }

    public List<String> getLogoutCommands() {
        return logoutCommands;
    }

    public boolean isTitleMessageEnabled() {
        return titleMessageEnabled;
    }
}
