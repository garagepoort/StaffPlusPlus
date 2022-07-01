package net.shortninja.staffplus.core.domain.blacklist.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.blacklist.BlacklistService;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import net.shortninja.staffplusplus.blacklist.BlacklistType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBukkitListener(conditionalOnProperty = "blacklist-module.enabled=true && blacklist-module.censor-chat=true")
public class BlacklistChatListener implements Listener {

    @ConfigProperty("permissions:blacklist")
    private String permissionBlacklist;
    @ConfigProperty("blacklist-module.hoverable")
    private boolean hoverable;

    private final BlacklistService blacklistService;
    private final PermissionHandler permission;
    private final IProtocolService protocolService;
    private final Options options;
    private final Messages messages;

    public BlacklistChatListener(BlacklistService blacklistService, PermissionHandler permission, IProtocolService protocolService, Options options, Messages messages) {
        this.blacklistService = blacklistService;
        this.permission = permission;
        this.protocolService = protocolService;
        this.options = options;
        this.messages = messages;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (permission.has(player, permissionBlacklist)) {
            return;
        }
        String originalMessage = event.getMessage();
        String censoredMessage = blacklistService.censorMessage(originalMessage);
        event.setMessage(censoredMessage);
        setHoverableMessage(player, event, originalMessage, censoredMessage);
        if (!originalMessage.equals(censoredMessage)) {
            sendEvent(new BlacklistCensoredEvent(options.serverName, player, censoredMessage, originalMessage, BlacklistType.CHAT));
        }
    }

    private void setHoverableMessage(Player player, AsyncPlayerChatEvent event, String originalMessage, String censoredMessage) {
        if (hoverable) {
            List<? extends Player> validPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(p -> permission.has(p, permissionBlacklist))
                .collect(Collectors.toList());

            validPlayers.forEach(event.getRecipients()::remove);
            Set<Player> staffPlayers = new HashSet<>(validPlayers);

            protocolService.getVersionProtocol().sendHoverableJsonMessage(staffPlayers, messages.blacklistChatFormat
                .replace("%player%", player.getName())
                .replace("%message%", censoredMessage), originalMessage);
        }
    }
}