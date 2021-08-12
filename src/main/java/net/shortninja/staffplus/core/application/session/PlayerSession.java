package net.shortninja.staffplus.core.application.session;

import net.shortninja.staffplus.core.common.gui.IGui;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.vanish.VanishType;

import java.util.Optional;
import java.util.UUID;

public class PlayerSession implements OnlinePlayerSession {

    private final UUID uuid;
    private String name;
    private IGui currentGui = null;
    private ChatAction chatAction = null;

    private String activeStaffChatChannel = null;
    private boolean isFrozen = false;
    private boolean underInvestigation = false;
    private boolean isProtected = false;
    private boolean muted = false;
    private boolean isInStaffMode = false;
    private GeneralModeConfiguration modeConfig;
    private VanishType vanishType = VanishType.NONE;

    public PlayerSession(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<IGui> getCurrentGui() {
        return Optional.ofNullable(currentGui);
    }

    public void setCurrentGui(IGui currentGui) {
        this.currentGui = currentGui;
    }

    @Override
    public ChatAction getChatAction() {
        return chatAction;
    }

    @Override
    public void setChatAction(ChatAction chatAction) {
        this.chatAction = chatAction;
    }

    @Override
    public boolean isInStaffMode() {
        return isInStaffMode;
    }

    @Override
    public void setInStaffMode(boolean inStaffMode) {
        isInStaffMode = inStaffMode;
    }

    @Override
    public Optional<GeneralModeConfiguration> getModeConfig() {
        return Optional.ofNullable(modeConfig);
    }

    @Override
    public void setModeConfig(GeneralModeConfiguration modeConfig) {
        this.modeConfig = modeConfig;
    }

    @Override
    public Optional<String> getActiveStaffChatChannel() {
        return Optional.ofNullable(activeStaffChatChannel);
    }

    @Override
    public void setActiveStaffChatChannel(String activeStaffChatChannel) {
        this.activeStaffChatChannel = activeStaffChatChannel;
    }

    @Override
    public boolean isFrozen() {
        return isFrozen;
    }

    @Override
    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    @Override
    public boolean isProtected() {
        return isProtected;
    }

    @Override
    public void setFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    @Override
    public VanishType getVanishType() {
        return vanishType;
    }

    @Override
    public void setVanishType(VanishType vanishType) {
        this.vanishType = vanishType;
    }


    public boolean isVanished() {
        return vanishType == VanishType.TOTAL || vanishType == VanishType.PLAYER;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    @Override
    public void setUnderInvestigation(boolean underInvestigation) {
        this.underInvestigation = underInvestigation;
    }

    @Override
    public boolean isUnderInvestigation() {
        return underInvestigation;
    }

    @Override
    public boolean isMuted() {
        return muted;
    }
}