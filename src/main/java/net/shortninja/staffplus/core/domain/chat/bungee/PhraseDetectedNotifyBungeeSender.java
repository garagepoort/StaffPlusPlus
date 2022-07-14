package net.shortninja.staffplus.core.domain.chat.bungee;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "isNotEmpty(alerts-module.chat-phrase-detection-bungee)")
public class PhraseDetectedNotifyBungeeSender implements Listener {

    @ConfigProperty("alerts-module.chat-phrase-detection-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public PhraseDetectedNotifyBungeeSender(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onPhraseDetected(PhrasesDetectedEvent phrasesDetectedEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, Constants.BUNGEE_PHRASE_DETECTION_ALERT_CHANNEL, new PhraseDetectedBungeeDto(phrasesDetectedEvent), syncServers);
    }
}