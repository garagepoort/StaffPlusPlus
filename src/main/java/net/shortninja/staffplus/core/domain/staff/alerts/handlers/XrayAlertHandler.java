package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplus.core.domain.staff.alerts.config.XrayConfiguration;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeDto;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeEvent;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@IocListener
public class XrayAlertHandler extends AlertsHandler implements Listener {

    private final XrayConfiguration xrayConfiguration;

    public XrayAlertHandler(AlertsConfiguration alertsConfiguration,
                            PlayerSettingsRepository playerSettingsRepository,
                            OnlineSessionsManager sessionManager,
                            PermissionHandler permission,
                            Messages messages,
                            XrayConfiguration xrayConfiguration,
                            PlayerManager playerManager) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        this.xrayConfiguration = xrayConfiguration;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        if (!alertsConfiguration.alertsXrayEnabled) {
            return;
        }
        if (permission.has(event.getPlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        List<String> enchantments = new ArrayList<>();
        event.getPickaxe().getEnchantments()
            .forEach((k,v) -> enchantments.add(JavaUtils.formatTypeName(k.getKey().getKey()) + " " + v));

        notifyPlayers(event.getPlayer().getName(),
            event.getAmount(),
            event.getType().name(),
            event.getLightLevel(),
            event.getDuration(),
            event.getServerName(),
            event.getPickaxe().getType().name(),
            enchantments);
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        if (!alertsConfiguration.alertsXrayEnabled) {
            return;
        }

        XrayAlertBungeeDto xrayAlertBungeeDto = event.getXrayAlertBungeeDto();
        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(xrayAlertBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent() && permission.has(sppPlayer.get().getOfflinePlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        notifyPlayers(xrayAlertBungeeDto.getPlayerName(),
            xrayAlertBungeeDto.getAmount(),
            xrayAlertBungeeDto.getType(),
            xrayAlertBungeeDto.getLightLevel(),
            xrayAlertBungeeDto.getDuration(),
            xrayAlertBungeeDto.getServerName(), "", Collections.emptyList());
    }

    private void notifyPlayers(String playerName,
                               int amount,
                               String type,
                               int lightLevel,
                               Optional<Long> duration,
                               String serverName,
                               String pickaxeType,
                               List<String> pickaxeEnchantments) {
        for (Player user : getPlayersToNotify()) {
            String xrayMessage = messages.alertsXray
                .replace("%target%", playerName)
                .replace("%count%", Integer.toString(amount))
                .replace("%server%", serverName)
                .replace("%itemtype%", JavaUtils.formatTypeName(type))
                .replace("%pickaxe-type%", JavaUtils.formatTypeName(pickaxeType))
                .replace("%pickaxe-enchantments%", String.join(" | ", pickaxeEnchantments))
                .replace("%lightlevel%", Integer.toString(lightLevel));

            if (duration.isPresent()) {
                xrayMessage = xrayMessage + String.format(" in %s seconds", duration.get() / 1000);
            }
            messages.send(user, xrayMessage, messages.prefixGeneral, xrayConfiguration.permissionXray);
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.XRAY;
    }

    @Override
    protected String getPermission() {
        return xrayConfiguration.permissionXray;
    }
}
