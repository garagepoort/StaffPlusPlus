package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
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

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.CHAT;

public class AsyncPlayerChat implements Listener {
    private final Options options = IocContainer.get(Options.class);
    private final List<ChatInterceptor> chatInterceptors = IocContainer.getList(ChatInterceptor.class);
    private final BlacklistService blacklistService = IocContainer.get(BlacklistService.class);
    private final TraceService traceService = IocContainer.get(TraceService.class);
    private final PlayerManager playerManager = IocContainer.get(PlayerManager.class);

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