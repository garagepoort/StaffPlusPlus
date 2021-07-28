package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionLoader;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.delayedactions.DelayedActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.UUID;

@IocBean
public class PlayerJoin implements Listener {


    @ConfigProperty("permissions:mode")
    private String permissionMode;

    private final PermissionHandler permission;
    private final Options options;
    private final SessionManagerImpl sessionManager;
    private final SessionLoader sessionLoader;
    private final StaffModeService staffModeService;
    private final VanishServiceImpl vanishServiceImpl;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;
    private final DelayedActionService delayedActionService;

    public PlayerJoin(PermissionHandler permission,
                      Options options,
                      SessionManagerImpl sessionManager,
                      SessionLoader sessionLoader,
                      StaffModeService staffModeService,
                      VanishServiceImpl vanishServiceImpl,
                      PlayerManager playerManager,
                      IProtocolService protocolService,
                      DelayedActionService delayedActionService) {
        this.permission = permission;
        this.options = options;
        this.sessionManager = sessionManager;
        this.sessionLoader = sessionLoader;
        this.staffModeService = staffModeService;
        this.vanishServiceImpl = vanishServiceImpl;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        this.delayedActionService = delayedActionService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        protocolService.getVersionProtocol().inject(event.getPlayer());
        playerManager.syncPlayer(event.getPlayer());

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            manageUser(player);
            PlayerSession session = sessionManager.get(player.getUniqueId());
            Optional<GeneralModeConfiguration> defaultMode = staffModeService.getModeConfig(player);
            Optional<GeneralModeConfiguration> modeConfiguration = session.getModeConfiguration().isPresent() ? session.getModeConfiguration() : defaultMode;
            if (modeConfiguration.isPresent() && permission.has(player, permissionMode) && (session.isInStaffMode() || modeConfiguration.get().isModeEnableOnLogin())) {
                staffModeService.turnStaffModeOn(player, modeConfiguration.get());
            } else {
                staffModeService.turnStaffModeOff(player);
            }
            vanishServiceImpl.updateVanish(player);

            if (session.isVanished()) {
                event.setJoinMessage("");
            }

            sessionLoader.saveSession(session);
            delayedActions(player);
        });

    }

    private void manageUser(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession playerSession = sessionManager.get(uuid);

        if (!playerSession.getName().equals(player.getName())) {
            BukkitUtils.sendEvent(new NameChangeEvent(options.serverName, player, playerSession.getName(), player.getName()));
            playerSession.setName(player.getName());
            sessionManager.saveSession(player);
        }
    }

    private void delayedActions(Player player) {
        delayedActionService.processDelayedAction(player);
    }
}