package net.shortninja.staffplus.core.domain.actions.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.load.InjectTubingPlugin;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class CommandCleanupTask extends BukkitRunnable {
    private static final int TIMER = 1800;

    private final StoredCommandRepository storedCommandRepository;

    public CommandCleanupTask(StoredCommandRepository storedCommandRepository, @InjectTubingPlugin TubingBukkitPlugin staffPlusPlus) {
        this.storedCommandRepository = storedCommandRepository;
        runTaskTimerAsynchronously(staffPlusPlus, TIMER * 20, TIMER * 20);
    }

    @Override
    public void run() {
        storedCommandRepository.deleteExecutedCommands();
    }
}