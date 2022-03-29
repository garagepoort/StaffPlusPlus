package net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee;

import be.garagepoort.mcioc.IocMessageListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocMessageListener(
    channel = BUNGEE_CORD_CHANNEL,
    conditionalOnProperty = "isNotEmpty(alerts-module.xray-alerts.notify-bungee)")
public class XrayAlertBungeeReceiver implements PluginMessageListener {

    @ConfigProperty("alerts-module.xray-alerts.notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public XrayAlertBungeeReceiver(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<XrayAlertBungeeDto> xrayAlertBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_XRAY_ALERT_CHANNEL, message, XrayAlertBungeeDto.class);

        if (xrayAlertBungeeDto.isPresent() && syncServers.matchesServer(xrayAlertBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new XrayAlertBungeeEvent(xrayAlertBungeeDto.get()));
        }
    }
}