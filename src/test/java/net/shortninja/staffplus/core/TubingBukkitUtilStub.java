package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.common.ITubingBukkitUtil;

public class TubingBukkitUtilStub implements ITubingBukkitUtil {
    @Override
    public void runAsync(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runTaskLater(Runnable runnable, int i) {
        runnable.run();
    }
}
