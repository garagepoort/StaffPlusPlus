package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.session.SessionManagerImpl;

@IocBean
@IocMultiProvider(PluginDisable.class)
public class StaffModePluginDisable implements PluginDisable {

    private final StaffModeService staffModeService;
    private final SessionManagerImpl sessionManager;

    public StaffModePluginDisable(StaffModeService staffModeService, SessionManagerImpl sessionManager) {
        this.staffModeService = staffModeService;
        this.sessionManager = sessionManager;
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        sessionManager.getAll().stream()
            .filter(p -> p.isInStaffMode() && p.getPlayer().isPresent())
            .filter(p -> p.getModeConfiguration().get().isModeDisableOnLogout())
            .map(p -> p.getPlayer().get())
            .forEach(staffModeService::removeMode);
    }
}
