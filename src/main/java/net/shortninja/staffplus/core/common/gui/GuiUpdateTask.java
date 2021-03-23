package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.UpdatableGui;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class GuiUpdateTask extends BukkitRunnable {
    private final SessionManagerImpl sessionManager;

    public GuiUpdateTask(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
        runTaskTimer(StaffPlus.get(), 0, 10);
    }

    @Override
    public void run() {
        for (PlayerSession playerSession : sessionManager.getAll()) {
            if (playerSession.getCurrentGui().isPresent() && playerSession.getCurrentGui().get() instanceof UpdatableGui) {
                ((UpdatableGui) playerSession.getCurrentGui().get()).update();
            }
        }
    }
}
