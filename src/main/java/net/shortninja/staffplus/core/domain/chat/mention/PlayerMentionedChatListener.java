package net.shortninja.staffplus.core.domain.chat.mention;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
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

@IocListener
public class PlayerMentionedChatListener implements Listener {
    private final Options options;
    private final PlayerManager playerManager;
    private final BukkitUtils bukkitUtils;
    private final MuteService muteService;

    public PlayerMentionedChatListener(Options options,
                                       PlayerManager playerManager,
                                       BukkitUtils bukkitUtils,
                                       MuteService muteService) {
        this.options = options;
        this.playerManager = playerManager;
        this.bukkitUtils = bukkitUtils;
        this.muteService = muteService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
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