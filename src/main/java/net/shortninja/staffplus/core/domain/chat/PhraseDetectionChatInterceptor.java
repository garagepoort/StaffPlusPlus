package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.chat.configuration.ChatConfiguration;
import net.shortninja.staffplus.core.domain.chat.configuration.PhraseDetectionGroupConfiguration;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class PhraseDetectionChatInterceptor implements ChatInterceptor {

    private final Options options;
    private final ChatConfiguration chatConfiguration;
    private final BukkitUtils bukkitUtils;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final PermissionHandler permission;

    @ConfigProperty("permissions:chat-phrase-detection-bypass")
    public String permissionChatPhraseDetectionBypass;

    public PhraseDetectionChatInterceptor(Options options,
                                          ChatConfiguration chatConfiguration,
                                          BukkitUtils bukkitUtils,
                                          ActionService actionService,
                                          ConfiguredCommandMapper configuredCommandMapper,
                                          PermissionHandler permission) {
        this.options = options;
        this.chatConfiguration = chatConfiguration;
        this.bukkitUtils = bukkitUtils;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
        this.permission = permission;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if(event.isCancelled() || permission.has(event.getPlayer(), permissionChatPhraseDetectionBypass)) {
            return false;

        }
        String message = event.getMessage();
        List<String> detectedPhrases = new ArrayList<>();

        for (PhraseDetectionGroupConfiguration detectionPhrase : chatConfiguration.detectionPhrases) {
            List<String> phrases = detectionPhrase.phrases.stream()
                .filter(phrase -> message.toLowerCase().contains(phrase.toLowerCase()))
                .collect(Collectors.toList());

            if (!phrases.isEmpty()) {
                executeActions(event.getPlayer(), detectionPhrase.actions, phrases, message);
            }

            detectedPhrases.addAll(phrases);
        }

        if (!detectedPhrases.isEmpty()) {
            sendEvent(new PhrasesDetectedEvent(options.serverName, event.getPlayer(), event.getMessage(), detectedPhrases));
        }
        return false;
    }

    private void executeActions(Player player, List<ConfiguredCommand> commands, List<String> detectedPhrases, String message) {
        if(commands.isEmpty()) {
            return;
        }
        bukkitUtils.runTaskAsync(() -> {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%player%", player.getName());
            placeholders.put("%phrases%", String.join(",", detectedPhrases));
            placeholders.put("%message%", message);

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
