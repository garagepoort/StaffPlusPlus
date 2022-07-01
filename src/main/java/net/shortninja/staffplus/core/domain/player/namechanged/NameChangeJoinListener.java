package net.shortninja.staffplus.core.domain.player.namechanged;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBukkitListener
public class NameChangeJoinListener implements Listener {
    @ConfigProperty("permissions:name-change-bypass")
    private String permissionNameChangeBypass;

    private final Options options;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final BukkitUtils bukkitUtils;
    private final PermissionHandler permissionHandler;

    public NameChangeJoinListener(Options options, PlayerSettingsRepository playerSettingsRepository, BukkitUtils bukkitUtils, PermissionHandler permissionHandler) {
        this.options = options;
        this.playerSettingsRepository = playerSettingsRepository;
        this.bukkitUtils = bukkitUtils;
        this.permissionHandler = permissionHandler;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(StaffPlusPlusJoinedEvent event) {
        Player player = event.getPlayerJoinEvent().getPlayer();
        OnlinePlayerSession session = event.getPlayerSession();

        if (!session.getName().equals(player.getName())) {
            bukkitUtils.runTaskAsync(event.getPlayer(), () -> {
                PlayerSettings playerSettings = event.getPlayerSettings();
                playerSettings.setName(player.getName());
                playerSettingsRepository.save(playerSettings);
                if(!permissionHandler.has(player, permissionNameChangeBypass)) {
                    sendEvent(new NameChangeEvent(options.serverName, player, session.getName(), player.getName()));
                }
            });
        }
    }
}
