package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.chat.ChatReceivePreventer;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.unordered.IPlayerSession;
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
    private final List<ChatPreventer> chatPreventers = IocContainer.getChatPreventers();
    private final List<ChatReceivePreventer> chatReceivePreventers = IocContainer.getChatReceivePreventers();
    private final BlacklistService blacklistService = IocContainer.getBlacklistService();
    private final TraceService traceService = IocContainer.getTraceService();

    public AsyncPlayerChat() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (shouldCancel(player, message)) {
            event.setCancelled(true);
            return;
        }

        chatReceivePreventers.forEach(preventer -> preventer.preventReceival(event));
        traceService.sendTraceMessage(CHAT, player.getUniqueId(), event.getMessage());

        List<IPlayerSession> mentioned = getMentioned(message);
        if (!mentioned.isEmpty()) {
            for (IPlayerSession user : mentioned) {
                alertCoordinator.onMention(user, player.getName());
            }
        }

        blacklistService.censorMessage(player, event);
    }

    private boolean shouldCancel(Player player, String message) {
        return chatPreventers.stream().anyMatch(c -> c.shouldPrevent(player, message));
    }

    private List<IPlayerSession> getMentioned(String message) {
        List<IPlayerSession> mentioned = new ArrayList<>();

        for (IPlayerSession user : sessionManager.getAll()) {
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