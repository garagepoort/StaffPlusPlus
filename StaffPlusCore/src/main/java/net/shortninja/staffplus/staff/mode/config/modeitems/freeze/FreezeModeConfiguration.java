package net.shortninja.staffplus.staff.mode.config.modeitems.freeze;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.util.lib.Sounds;

import java.util.List;

public class FreezeModeConfiguration extends ModeItemConfiguration {

    private int modeFreezeTimer;
    private Sounds modeFreezeSound;
    private boolean modeFreezePrompt;
    private String modeFreezePromptTitle;
    private boolean modeFreezeChat;
    private boolean modeFreezeDamage;
    private final List<String> logoutCommands;

    public FreezeModeConfiguration(int modeFreezeTimer, Sounds modeFreezeSound, boolean modeFreezePrompt, String modeFreezePromptTitle, boolean modeFreezeChat, boolean modeFreezeDamage, List<String> logoutCommands) {
        this.modeFreezeTimer = modeFreezeTimer;
        this.modeFreezeSound = modeFreezeSound;
        this.modeFreezePrompt = modeFreezePrompt;
        this.modeFreezePromptTitle = modeFreezePromptTitle;
        this.modeFreezeChat = modeFreezeChat;
        this.modeFreezeDamage = modeFreezeDamage;
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
}
