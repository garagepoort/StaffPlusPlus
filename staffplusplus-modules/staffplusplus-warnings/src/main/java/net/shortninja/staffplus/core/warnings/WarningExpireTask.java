package net.shortninja.staffplus.core.warnings;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.warnings.config.WarningConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class WarningExpireTask extends BukkitRunnable {

    private static final int DELAY = 20 * 30;
    private final WarnService warnService;

    public WarningExpireTask(WarnService warnService, WarningConfiguration warningConfiguration) {
        this.warnService = warnService;
        if (warningConfiguration.getSeverityLevels().stream().anyMatch(s -> s.getExpirationDuration() > 0)) {
            runTaskTimerAsynchronously(TubingBukkitPlugin.getPlugin(), DELAY, DELAY);
        }
    }

    @Override
    public void run() {
        warnService.expireWarnings();
    }

}