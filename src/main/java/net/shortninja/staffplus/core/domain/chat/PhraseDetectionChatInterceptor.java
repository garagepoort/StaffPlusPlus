package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.chat.configuration.ChatConfiguration;
import net.shortninja.staffplus.core.domain.chat.configuration.PhraseDetectionGroupConfiguration;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class PhraseDetectionChatInterceptor implements ChatInterceptor {

    private final Options options;
    private final ChatConfiguration chatConfiguration;
    private final BukkitUtils bukkitUtils;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private PlayerManager playerManager;

    public PhraseDetectionChatInterceptor(Options options, ChatConfiguration chatConfiguration, BukkitUtils bukkitUtils, ActionService actionService, ConfiguredCommandMapper configuredCommandMapper) {
        this.options = options;
        this.chatConfiguration = chatConfiguration;
        this.bukkitUtils = bukkitUtils;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        List<String> detectedPhrases = new ArrayList<>();

        for (PhraseDetectionGroupConfiguration detectionPhrase : chatConfiguration.detectionPhrases) {
            List<String> phrases = detectionPhrase.phrases.stream()
                .filter(phrase -> message.toLowerCase().contains(phrase.toLowerCase()))
                .collect(Collectors.toList());

            if (!phrases.isEmpty()) {
                executeActions(event.getPlayer(), detectionPhrase.commands);
            }

            detectedPhrases.addAll(phrases);
        }

        if (!detectedPhrases.isEmpty()) {
            BukkitUtils.sendEvent(new PhrasesDetectedEvent(options.serverName, event.getPlayer(), event.getMessage(), detectedPhrases));
        }
        return false;
    }

    private void executeActions(Player player, List<ConfiguredCommand> commands) {
        bukkitUtils.runTaskAsync(() -> {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%player%", player.getName());

            Map<String, OfflinePlayer> targets = new HashMap<>();
            targets.put("player", player);

            actionService.createCommands(configuredCommandMapper.toCreateRequests(commands, placeholders, targets, new ArrayList<>()));
        });
    }

    @Override
    public int getPriority() {
        return 7;
    }
}
