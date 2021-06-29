package net.shortninja.staffplus.core.application.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class SessionSaveOnPluginDisable implements PluginDisable {

    private final SessionManagerImpl sessionManager;

    public SessionSaveOnPluginDisable(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        StaffPlus.get().getLogger().info("Saving all sessions...");
        sessionManager.saveAll();
    }
}