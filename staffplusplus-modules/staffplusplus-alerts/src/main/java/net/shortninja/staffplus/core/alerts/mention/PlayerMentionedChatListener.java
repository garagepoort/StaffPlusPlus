package net.shortninja.staffplus.core.alerts.mention;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.punishments.mute.Mute;
import net.shortninja.staffplus.core.punishments.mute.MuteService;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBukkitListener
public class PlayerMentionedChatListener implements Listener {
    @ConfigProperty("permissions:mention-bypass")
    private String permissionMentionBypass;
    private final Options options;
    private final PlayerManager playerManager;
    private final BukkitUtils bukkitUtils;
    private final MuteService muteService;
    private final PermissionHandler permissionHandler;

    public PlayerMentionedChatListener(Options options,
                                       PlayerManager playerManager,
                                       BukkitUtils bukkitUtils,
                                       MuteService muteService, PermissionHandler permissionHandler) {
        this.options = options;
        this.playerManager = playerManager;
        this.bukkitUtils = bukkitUtils;
        this.muteService = muteService;
        this.permissionHandler = permissionHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (permissionHandler.has(player, permissionMentionBypass)) {
            return;
        }
        String message = event.getMessage();
        notifyMentioned(player, message);
    }

    private void notifyMentioned(Player player, String message) {
        bukkitUtils.runTaskAsync(() -> {
            Optional<Mute> muteByMutedUuid = muteService.getMuteByMutedUuid(player.getUniqueId());
            if (!muteByMutedUuid.isPresent()) {
                getMentioned(message).stream()
                    .map(user -> new PlayerMentionedEvent(options.serverName, player, user, message))
                    .forEach(BukkitUtils::sendEvent);
            }
        });
    }

    private List<OfflinePlayer> getMentioned(String message) {
        return playerManager.getOnAndOfflinePlayers().stream()
            .filter(offlinePlayer -> message.toLowerCase().contains(offlinePlayer.getUsername().toLowerCase()))
            .map(p -> Bukkit.getOfflinePlayer(p.getId()))
            .collect(Collectors.toList());
    }
}