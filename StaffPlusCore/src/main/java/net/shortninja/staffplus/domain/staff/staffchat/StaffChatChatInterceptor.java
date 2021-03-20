package net.shortninja.staffplus.domain.staff.staffchat;

import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChatChatInterceptor implements ChatInterceptor {
    private final StaffChatService staffChatService;
    private final PermissionHandler permission;
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public StaffChatChatInterceptor(StaffChatService staffChatService, PermissionHandler permission, Options options, SessionManagerImpl sessionManager) {
        this.staffChatService = staffChatService;
        this.permission = permission;
        this.options = options;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if(!options.staffChatConfiguration.isEnabled()) {
            return false;
        }

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
