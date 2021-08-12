package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.UpdatableGui;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

@IocBean
public class GuiUpdateTask extends BukkitRunnable {
    private final OnlineSessionsManager sessionManager;

    public GuiUpdateTask(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
        runTaskTimer(StaffPlus.get(), 0, 10);
    }

    @Override
    public void run() {
        for (OnlinePlayerSession playerSession : sessionManager.getAll()) {
            Optional<IGui> currentGui = playerSession.getCurrentGui();
            if (currentGui.isPresent() && currentGui.get() instanceof UpdatableGui) {
                ((UpdatableGui) currentGui.get()).update();
            }
        }
    }
}
