package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.CommandUtil;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.staffchat.StaffChatService;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class StaffChatCmd extends BukkitCommand {
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final UserManager userManager = IocContainer.getUserManager();
    private final StaffChatService staffChatService = IocContainer.getStaffChatService();

    public StaffChatCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return CommandUtil.executeCommand(sender, true, () -> {
            if (!permission.has(sender, options.permissionStaffChat)) {
                message.send(sender, messages.noPermission, messages.prefixStaffChat);
                return true;
            }

            if (args.length > 0) {
                staffChatService.sendMessage(sender, JavaUtils.compileWords(args, 0));
            } else if (sender instanceof Player) {
                IUser user = userManager.get(((Player) sender).getUniqueId());

                if (user.inStaffChatMode()) {
                    message.send(sender, messages.staffChatStatus.replace("%status%", messages.disabled), messages.prefixStaffChat);
                    user.setChatting(false);
                } else {
                    message.send(sender, messages.staffChatStatus.replace("%status%", messages.enabled), messages.prefixStaffChat);
                    user.setChatting(true);
                }
            } else message.send(sender, messages.onlyPlayers, messages.prefixStaffChat);

            return true;
        });
    }
}