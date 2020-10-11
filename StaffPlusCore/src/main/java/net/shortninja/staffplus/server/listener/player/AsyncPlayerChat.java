package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.staff.alerts.AlertCoordinator;
import net.shortninja.staffplus.server.chat.ChatInterceptor;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.staff.tracing.TraceType.CHAT;

public class AsyncPlayerChat implements Listener {
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final AlertCoordinator alertCoordinator = IocContainer.getAlertCoordinator();
    private final List<ChatInterceptor> chatInterceptors = IocContainer.getChatInterceptors();
    private final BlacklistService blacklistService = IocContainer.getBlacklistService();
    private final TraceService traceService = IocContainer.getTraceService();

    public AsyncPlayerChat() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        for (ChatInterceptor chatInterceptor : chatInterceptors) {
            boolean cancel = chatInterceptor.intercept(event);
            if(cancel) {
                event.setCancelled(true);
                return;
            }
        }

        traceService.sendTraceMessage(CHAT, player.getUniqueId(), event.getMessage());

        List<PlayerSession> mentioned = getMentioned(message);
        if (!mentioned.isEmpty()) {
            for (PlayerSession user : mentioned) {
                alertCoordinator.onMention(user, player.getName());
            }
        }

        blacklistService.censorMessage(player, event);
    }

    private List<PlayerSession> getMentioned(String message) {
        List<PlayerSession> mentioned = new ArrayList<>();

        for (PlayerSession user : sessionManager.getAll()) {
            if (!user.getPlayer().isPresent()) {
                continue; // How?
            }

            if (message.toLowerCase().contains(user.getName().toLowerCase())) {
                mentioned.add(user);
            }
        }

        return mentioned;
    }
}