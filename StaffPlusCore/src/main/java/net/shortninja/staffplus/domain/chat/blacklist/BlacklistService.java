package net.shortninja.staffplus.domain.chat.blacklist;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.domain.chat.blacklist.censors.ChatCensor;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class BlacklistService {

    private final IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private final Options options;
    private final PermissionHandler permission;
    private final Messages messages;
    private final List<ChatCensor> chatCensors;

    public BlacklistService(Options options, PermissionHandler permission, Messages messages, List<ChatCensor> chatCensors) {
        this.options = options;
        this.permission = permission;
        this.messages = messages;
        this.chatCensors = chatCensors;
    }

    public void censorMessage(Player player, AsyncPlayerChatEvent event) {
        if (permission.has(player, options.permissionBlacklist)) {
            return;
        }

        if (options.blackListConfiguration.isEnabled() && options.chatConfiguration.isChatEnabled()) {
            String originalMessage = event.getMessage();
            String censoredMessage = originalMessage;
            for (ChatCensor chatCensor : chatCensors) {
                censoredMessage = chatCensor.censor(censoredMessage);
            }
            event.setMessage(censoredMessage);
            setHoverableMessage(player, event, originalMessage, censoredMessage);
        }
    }

    private void setHoverableMessage(Player player, AsyncPlayerChatEvent event, String originalMessage, String censoredMessage) {
        if (options.blackListConfiguration.isHoverable()) {
            List<? extends Player> validPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(p -> permission.has(p, options.permissionBlacklist))
                .collect(Collectors.toList());

            event.getRecipients().removeAll(validPlayers);
            Set<Player> staffPlayers = new HashSet<>(validPlayers);

            versionProtocol.sendHoverableJsonMessage(staffPlayers, messages.blacklistChatFormat.replace("%player%", player.getName()).replace("%message%", censoredMessage), originalMessage);
        }
    }
}