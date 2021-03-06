package net.shortninja.staffplus.staff.warn.warnings;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.scheduler.BukkitRunnable;

public class WarningExpireTask extends BukkitRunnable {

    private static final int DELAY = 20 * 30;

    public WarningExpireTask() {
        Options options = IocContainer.getOptions();
        if (options.warningConfiguration.getSeverityLevels().stream().anyMatch(s -> s.getExpirationDuration() > 0)) {
            runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
        }
    }

    @Override
    public void run() {
        IocContainer.getWarnService().expireWarnings();
    }

}