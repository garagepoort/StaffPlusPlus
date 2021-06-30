package net.shortninja.staffplus.core.domain.staff.staffchat;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.staff.staffchat.config.StaffChatConfiguration;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChatChatInterceptor implements ChatInterceptor {
    private final StaffChatServiceImpl staffChatService;
    private final PermissionHandler permission;
    private final SessionManagerImpl sessionManager;
    private final StaffChatChannelConfiguration channelConfiguration;
    private final StaffChatConfiguration staffChatConfiguration;

    public StaffChatChatInterceptor(StaffChatServiceImpl staffChatService, PermissionHandler permission, SessionManagerImpl sessionManager, StaffChatChannelConfiguration channelConfiguration, StaffChatConfiguration staffChatConfiguration) {
        this.staffChatService = staffChatService;
        this.permission = permission;
        this.sessionManager = sessionManager;
        this.channelConfiguration = channelConfiguration;
        this.staffChatConfiguration = staffChatConfiguration;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if(!staffChatConfiguration.isEnabled()) {
            return false;
        }

        PlayerSession session = sessionManager.get(event.getPlayer().getUniqueId());

        if (session.getActiveStaffChatChannel().isPresent() && session.getActiveStaffChatChannel().get().equalsIgnoreCase(channelConfiguration.getName())) {
            staffChatService.sendMessage(event.getPlayer(), session.getActiveStaffChatChannel().get(), event.getMessage());
            return true;
        }

        if (staffChatService.hasHandle(channelConfiguration.getName(), event.getMessage()) && permission.has(event.getPlayer(), channelConfiguration.getPermission().orElse(null))) {
            staffChatService.sendMessage(event.getPlayer(), channelConfiguration.getName(), event.getMessage().substring(channelConfiguration.getHandle().orElse("").length()));
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 6;
    }
}
