package net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_XRAY_ALERT_CHANNEL;

@IocBukkitListener(conditionalOnProperty = "isNotEmpty(alerts-module.xray-alerts.notify-bungee)")
public class XrayAlertBungeeSender implements Listener {

    @ConfigProperty("alerts-module.xray-alerts.notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public XrayAlertBungeeSender(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onXrayAlert(XrayEvent xrayEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();

        Map<String, Integer> enchantments = new HashMap<>();
        xrayEvent.getPickaxe().getEnchantments()
            .forEach((k, v) -> enchantments.put(k.getName(), v));

        bungeeClient.sendMessage(
            player,
            BUNGEE_XRAY_ALERT_CHANNEL,
            new XrayAlertBungeeDto(xrayEvent, xrayEvent.getPickaxe().getType().name(), enchantments), syncServers);
    }
}