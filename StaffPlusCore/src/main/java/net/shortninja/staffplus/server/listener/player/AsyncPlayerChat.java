package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.chat.ChatReceivePreventer;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.unordered.IUser;
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
    private final UserManager userManager = IocContainer.getUserManager();
    private final AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
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

        List<IUser> mentioned = getMentioned(message);
        if (!mentioned.isEmpty()) {
            for (IUser user : mentioned) {
                alertCoordinator.onMention(user, player.getName());
            }
        }

        blacklistService.censorMessage(player, event);
    }

    private boolean shouldCancel(Player player, String message) {
        return chatPreventers.stream().anyMatch(c -> c.shouldPrevent(player, message));
    }

    private List<IUser> getMentioned(String message) {
        List<IUser> mentioned = new ArrayList<>();

        for (IUser user : userManager.getAll()) {
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