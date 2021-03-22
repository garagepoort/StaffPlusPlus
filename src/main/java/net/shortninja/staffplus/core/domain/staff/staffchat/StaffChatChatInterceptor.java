package net.shortninja.staffplus.core.domain.staff.staffchat;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class StaffChatChatInterceptor implements ChatInterceptor {
    private final StaffChatServiceImpl staffChatService;
    private final PermissionHandler permission;
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public StaffChatChatInterceptor(StaffChatServiceImpl staffChatService, PermissionHandler permission, Options options, SessionManagerImpl sessionManager) {
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
