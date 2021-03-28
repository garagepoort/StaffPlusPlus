package net.shortninja.staffplus.core.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class SessionPluginDisable implements PluginDisable {
    private final SessionManagerImpl sessionManager;
    private final SessionLoader sessionLoader;

    public SessionPluginDisable(SessionManagerImpl sessionManager, SessionLoader sessionLoader) {
        this.sessionManager = sessionManager;
        this.sessionLoader = sessionLoader;
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        sessionManager.getAll().forEach(sessionLoader::saveSessionSynchronous);
    }
}
