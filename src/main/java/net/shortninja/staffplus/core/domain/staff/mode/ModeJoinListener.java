package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

@IocBean
@IocListener
public class ModeJoinListener implements Listener {

    @ConfigProperty("permissions:mode")
    private String permissionMode;

    private final StaffModeService staffModeService;
    private final SessionManagerImpl sessionManager;
    private final PermissionHandler permission;
    private final BukkitUtils bukkitUtils;

    public ModeJoinListener(StaffModeService staffModeService, SessionManagerImpl sessionManager, PermissionHandler permission, BukkitUtils bukkitUtils) {
        this.staffModeService = staffModeService;
        this.sessionManager = sessionManager;
        this.permission = permission;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        bukkitUtils.runTaskAsync(player, () -> {
            PlayerSession session = sessionManager.get(player.getUniqueId());

            Optional<GeneralModeConfiguration> defaultMode = staffModeService.getModeConfig(player);
            Optional<GeneralModeConfiguration> modeConfiguration = session.getModeConfiguration().isPresent() ? session.getModeConfiguration() : defaultMode;
            if (modeConfiguration.isPresent() && permission.has(player, permissionMode) && (session.isInStaffMode() || modeConfiguration.get().isModeEnableOnLogin())) {
                staffModeService.turnStaffModeOn(player, modeConfiguration.get());
            } else {
                staffModeService.turnStaffModeOff(player);
            }
        });
    }
}
