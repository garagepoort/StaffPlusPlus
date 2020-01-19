package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.handler.FreezeHandler;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.chat.BlacklistFactory;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class AsyncPlayerChat implements Listener {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().getUserManager();
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
    private ChatHandler chatHandler = StaffPlus.get().chatHandler;
    private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;

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

        if (options.chatBlacklistEnabled && options.chatEnabled) {
            BlacklistFactory blacklistFactory = new BlacklistFactory(message);

            if (blacklistFactory.runCheck().hasChanged() && !permission.has(player, options.permissionBlacklist)) {
                event.setMessage(blacklistFactory.getResult());

                if (options.chatBlacklistHoverable) {
                    Set<Player> staffPlayers = new HashSet<Player>();

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (permission.has(p, options.permissionBlacklist)) {
                            event.getRecipients().remove(p);
                            staffPlayers.add(p);
                        }
                    }

                    versionProtocol.sendHoverableJsonMessage(staffPlayers, messages.blacklistChatFormat.replace("%player%", player.getName()).replace("%message%", blacklistFactory.getResult()), message);
                }
            }
        }
    }

    private boolean shouldCancel(Player player, String message) {
        boolean shouldCancel = false;
        UUID uuid = player.getUniqueId();
        IUser user = userManager.get(uuid);
        IAction queuedAction = user.getQueuedAction();

        if (queuedAction != null) {
            queuedAction.execute(player, message);
            user.setQueuedAction(null);
            shouldCancel = true;
        } else if (freezeHandler.isFrozen(uuid) && (!options.modeFreezeChat || freezeHandler.isLoggedOut(uuid))) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
            shouldCancel = true;
        } else if (user.isChatting()) {
            chatHandler.sendStaffChatMessage(player.getName(), message);
            shouldCancel = true;
        } else if (chatHandler.hasHandle(message) && permission.has(player, options.permissionStaffChat)) {
            chatHandler.sendStaffChatMessage(player.getName(), message.substring(1));
            shouldCancel = true;
        } else if (!chatHandler.canChat(player)) {
            this.message.send(player, messages.chattingFast, messages.prefixGeneral);
            shouldCancel = true;
        } else if (!chatHandler.isChatEnabled(player) || (user.isFrozen() && !options.modeFreezeChat)) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
            shouldCancel = true;
        } else if (StaffPlus.get().options.vanishEnabled && !StaffPlus.get().options.vanishChatEnabled && StaffPlus.get().vanishHandler.isVanished(player)) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
            shouldCancel = true;
        }

        return shouldCancel;
    }

    private List<IUser> getMentioned(String message) {
        List<IUser> mentioned = new ArrayList<>();

        for (IUser user : userManager.getAll()) {
            if (!user.getPlayer().isPresent()) {
                continue; // How?
            }

            Player player = user.getPlayer().get();

            if (message.toLowerCase().contains(user.getName().toLowerCase())) {
                mentioned.add(user);
            }
        }

        return mentioned;
    }
}