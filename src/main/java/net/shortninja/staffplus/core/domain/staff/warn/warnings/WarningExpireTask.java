package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class WarningExpireTask extends BukkitRunnable {

    private static final int DELAY = 20 * 30;
    private final WarnService warnService;

    public WarningExpireTask(Options options, WarnService warnService, WarningConfiguration warningConfiguration) {
        this.warnService = warnService;
        if (warningConfiguration.getSeverityLevels().stream().anyMatch(s -> s.getExpirationDuration() > 0)) {
            runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
        }
    }

    @Override
    public void run() {
        warnService.expireWarnings();
    }

}