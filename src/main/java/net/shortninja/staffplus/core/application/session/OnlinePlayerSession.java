package net.shortninja.staffplus.core.application.session;

import net.shortninja.staffplus.core.common.gui.IGui;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.IPlayerSession;
import net.shortninja.staffplusplus.vanish.VanishType;

import java.util.Optional;

public interface OnlinePlayerSession extends IPlayerSession {

    Optional<IGui> getCurrentGui();

    void setCurrentGui(IGui abstractGui);

    ChatAction getChatAction();

    void setChatAction(ChatAction chatAction);

    void setInStaffMode(boolean inStaffMode);

    Optional<GeneralModeConfiguration> getModeConfig();

    void setModeConfig(GeneralModeConfiguration modeConfig);

    void setFrozen(boolean frozen);

    void setVanishType(VanishType vanishType);

    void setName(String name);

    boolean isCanViewStyleIds();
}
