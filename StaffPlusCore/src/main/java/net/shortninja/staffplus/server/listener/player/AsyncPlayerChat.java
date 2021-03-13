package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.chat.ChatInterceptor;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.util.BukkitUtils;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.staff.tracing.TraceType.CHAT;

public class AsyncPlayerChat implements Listener {
    private final Options options = IocContainer.getOptions();
    private final List<ChatInterceptor> chatInterceptors = IocContainer.getChatInterceptors();
    private final BlacklistService blacklistService = IocContainer.getBlacklistService();
    private final TraceService traceService = IocContainer.getTraceService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();

    public AsyncPlayerChat() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        for (ChatInterceptor chatInterceptor : chatInterceptors) {
            boolean cancel = chatInterceptor.intercept(event);
            if (cancel) {
                event.setCancelled(true);
                return;
            }
        }

        traceService.sendTraceMessage(CHAT, player.getUniqueId(), event.getMessage());

        List<OfflinePlayer> mentioned = getMentioned(message);
        for (OfflinePlayer user : mentioned) {
            BukkitUtils.sendEvent(new PlayerMentionedEvent(options.serverName, player, user, message));
        }

        blacklistService.censorMessage(player, event);
    }

    private List<OfflinePlayer> getMentioned(String message) {
        return playerManager.getAllPLayers().stream()
            .filter(offlinePlayer -> message.toLowerCase().contains(offlinePlayer.getName().toLowerCase()))
            .collect(Collectors.toList());
    }
}