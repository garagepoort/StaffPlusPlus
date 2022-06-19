package net.shortninja.staffplus.core.domain.chat.bungee;

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
    conditionalOnProperty = "isNotEmpty(alerts-module.chat-phrase-detection-bungee)")
public class PhraseDetectedBungeeReceiver implements PluginMessageListener {

    @ConfigProperty("alerts-module.chat-phrase-detection-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public PhraseDetectedBungeeReceiver(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<PhraseDetectedBungeeDto> phraseDetectedBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_PHRASE_DETECTION_ALERT_CHANNEL, message, PhraseDetectedBungeeDto.class);

        if (phraseDetectedBungeeDto.isPresent() && syncServers.matchesServer(phraseDetectedBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new PhraseDetectedBungeeEvent(phraseDetectedBungeeDto.get()));
        }
    }
}