package net.shortninja.staffplus.core.bootstrap;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.mode.handler.GadgetHandler;
import org.bukkit.scheduler.BukkitRunnable;

@IocBean
public class StaffModeTasks extends BukkitRunnable {

    @ConfigProperty("clock")
    private long clock;

    private final GadgetHandler gadgetHandler;

    public StaffModeTasks(GadgetHandler gadgetHandler) {
        this.gadgetHandler = gadgetHandler;
        runTaskTimerAsynchronously(TubingBukkitPlugin.getPlugin(), clock * 20, clock * 20);
    }

    @Override
    public void run() {
        gadgetHandler.updateGadgets();
    }
}