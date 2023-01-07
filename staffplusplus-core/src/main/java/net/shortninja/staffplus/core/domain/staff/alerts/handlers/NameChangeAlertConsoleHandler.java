package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.namechanged.bungee.NameChangeBungeeDto;
import net.shortninja.staffplus.core.domain.player.namechanged.bungee.NameChangedBungeeEvent;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBukkitListener(conditionalOnProperty = "alerts-module.name-notify-console=true")
public class NameChangeAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;

    public NameChangeAlertConsoleHandler(Messages messages, PlayerManager playerManager) {
        this.messages = messages;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        log(nameChangeEvent.getOldName(), nameChangeEvent.getServerName(), nameChangeEvent.getNewName());
    }

    @EventHandler
    public void handle(NameChangedBungeeEvent nameChangeEvent) {
        NameChangeBungeeDto nameChangeBungeeDto = nameChangeEvent.getNameChangeBungeeDto();
        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(nameChangeBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent()) {
            log(nameChangeBungeeDto.getOldName(), nameChangeBungeeDto.getServerName(), nameChangeBungeeDto.getNewName());
        }
    }

    private void log(String oldName, String serverName, String newName) {
        String message = messages.alertsName
            .replace("%old%", oldName)
            .replace("%server%", serverName)
            .replace("%new%", newName);
        TubingBukkitPlugin.getPlugin().getLogger().info(message);
    }
}
