package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.unordered.IUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class AsyncPlayerChat implements Listener {
    private final UserManager userManager = IocContainer.getUserManager();
    private final AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
    private final List<ChatPreventer> chatPreventers = IocContainer.getChatPreventers();

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

        List<IUser> mentioned = getMentioned(message);

        if (!mentioned.isEmpty()) {
            for (IUser user : mentioned) {
                alertCoordinator.onMention(user, player.getName());
            }
        }

        IocContainer.getBlacklistService().censorMessage(player, event);
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