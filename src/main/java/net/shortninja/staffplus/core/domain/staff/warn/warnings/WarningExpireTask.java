package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import org.bukkit.scheduler.BukkitRunnable;

public class WarningExpireTask extends BukkitRunnable {

    private static final int DELAY = 20 * 30;

    public WarningExpireTask() {
        Options options = IocContainer.get(Options.class);
        if (options.warningConfiguration.getSeverityLevels().stream().anyMatch(s -> s.getExpirationDuration() > 0)) {
            runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
        }
    }

    @Override
    public void run() {
        IocContainer.get(WarnService.class).expireWarnings();
    }

}