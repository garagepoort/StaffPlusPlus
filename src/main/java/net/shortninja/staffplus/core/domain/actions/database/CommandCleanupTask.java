package net.shortninja.staffplus.core.domain.actions.database;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class CommandCleanupTask extends BukkitRunnable {
    private static final int TIMER = 1800;

    private final StoredCommandRepository storedCommandRepository;

    public CommandCleanupTask(StoredCommandRepository storedCommandRepository) {
        this.storedCommandRepository = storedCommandRepository;
        runTaskTimerAsynchronously(StaffPlus.get(), TIMER * 20, TIMER * 20);
    }

    @Override
    public void run() {
        storedCommandRepository.deleteExecutedCommands();
    }
}