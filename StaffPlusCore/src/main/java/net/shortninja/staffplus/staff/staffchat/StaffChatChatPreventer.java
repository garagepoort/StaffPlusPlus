package net.shortninja.staffplus.staff.staffchat;

import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.entity.Player;

public class StaffChatChatPreventer implements ChatPreventer {
    private final StaffChatService staffChatService;
    private final PermissionHandler permission;
    private final Options options;
    private final UserManager userManager;

    public StaffChatChatPreventer(StaffChatService staffChatService, PermissionHandler permission, Options options, UserManager userManager) {
        this.staffChatService = staffChatService;
        this.permission = permission;
        this.options = options;
        this.userManager = userManager;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        IUser user = userManager.get(player.getUniqueId());

        if (user.inStaffChatMode()) {
            staffChatService.sendMessage(player, message);
            return true;
        }

        if (staffChatService.hasHandle(message) && permission.has(player, options.permissionStaffChat)) {
            staffChatService.sendMessage(player, message.substring(1));
            return true;
        }
        return false;
    }
}
