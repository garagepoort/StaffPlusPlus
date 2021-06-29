package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class WarningExpireTask extends BukkitRunnable {

    private static final int DELAY = 20 * 30;
    private final WarnService warnService;

    public WarningExpireTask(Options options, WarnService warnService) {
        this.warnService = warnService;
        if (options.warningConfiguration.getSeverityLevels().stream().anyMatch(s -> s.getExpirationDuration() > 0)) {
            runTaskTimerAsynchronously(StaffPlus.get(), DELAY, DELAY);
        }
    }

    @Override
    public void run() {
        warnService.expireWarnings();
    }

}