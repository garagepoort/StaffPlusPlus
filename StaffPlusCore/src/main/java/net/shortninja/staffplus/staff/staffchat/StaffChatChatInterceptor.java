package net.shortninja.staffplus.staff.staffchat;

import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.server.chat.ChatInterceptor;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChatChatInterceptor implements ChatInterceptor {
    private final StaffChatService staffChatService;
    private final PermissionHandler permission;
    private final Options options;
    private final SessionManager sessionManager;

    public StaffChatChatInterceptor(StaffChatService staffChatService, PermissionHandler permission, Options options, SessionManager sessionManager) {
        this.staffChatService = staffChatService;
        this.permission = permission;
        this.options = options;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        PlayerSession session = sessionManager.get(event.getPlayer().getUniqueId());

        if (session.inStaffChatMode()) {
            staffChatService.sendMessage(event.getPlayer(), event.getMessage());
            return true;
        }

        if (staffChatService.hasHandle(event.getMessage()) && permission.has(event.getPlayer(), options.staffChatConfiguration.getPermissionStaffChat())) {
            staffChatService.sendMessage(event.getPlayer(), event.getMessage().substring(1));
            return true;
        }
        return false;
    }
}
