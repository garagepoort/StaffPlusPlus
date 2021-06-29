package net.shortninja.staffplus.core.application.session;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class SessionSaveTask extends BukkitRunnable {

    private static final int DELAY = 20 * 60 * 5;
    private final SessionManagerImpl sessionManager;

    public SessionSaveTask(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
        runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
    }

    @Override
    public void run() {
        sessionManager.saveAll();
    }

}