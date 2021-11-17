package net.shortninja.staffplus.core.common.gui.style;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.style.TubingGuiStyleIdViewProvider;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;

@IocBean
public class StaffTubingGuiStyleIdViewProvider implements TubingGuiStyleIdViewProvider {

    private final OnlineSessionsManager onlineSessionsManager;

    public StaffTubingGuiStyleIdViewProvider(OnlineSessionsManager onlineSessionsManager) {
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @Override
    public boolean canView(Player player) {
        return onlineSessionsManager.get(player).isCanViewStyleIds();
    }
}
