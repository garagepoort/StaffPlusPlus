package net.shortninja.staffplus.core.domain.staff.alerts;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocListener
public class NameChangeJoinListener implements Listener {

    private final Options options;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final BukkitUtils bukkitUtils;

    public NameChangeJoinListener(Options options, PlayerSettingsRepository playerSettingsRepository, BukkitUtils bukkitUtils) {
        this.options = options;
        this.playerSettingsRepository = playerSettingsRepository;
        this.bukkitUtils = bukkitUtils;
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
                sendEvent(new NameChangeEvent(options.serverName, player, session.getName(), player.getName()));
            });
        }
    }
}
