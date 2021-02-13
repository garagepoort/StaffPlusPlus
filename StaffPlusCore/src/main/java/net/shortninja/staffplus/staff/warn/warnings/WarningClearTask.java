package net.shortninja.staffplus.staff.warn.warnings;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class WarningClearTask extends BukkitRunnable {
    private final Options options = IocContainer.getOptions();

    public WarningClearTask() {
        runTaskTimerAsynchronously(StaffPlus.get(), options.clock, options.clock);
    }

    @Override
    public void run() {
        checkWarnings();
    }

    private void checkWarnings() {
        for (IWarning warning : IocContainer.getWarnService().getWarnings()) {
            if (warning.shouldRemove()) {
                IocContainer.getWarnService().removeWarning(Bukkit.getConsoleSender(), warning.getId());
            }
        }
    }
}