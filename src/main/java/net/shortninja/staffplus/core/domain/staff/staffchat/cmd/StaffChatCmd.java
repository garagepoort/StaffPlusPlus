package net.shortninja.staffplus.core.domain.staff.staffchat.cmd;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.staffchat.StaffChatServiceImpl;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class StaffChatCmd extends AbstractCmd {
    private final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final StaffChatServiceImpl staffChatService = IocContainer.get(StaffChatServiceImpl.class);

    public StaffChatCmd(String name) {
        super(name, IocContainer.get(Options.class).staffChatConfiguration.getPermissionStaffChat());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (args.length > 0) {
            staffChatService.sendMessage(sender, JavaUtils.compileWords(args, 0));
        } else {
            if(!(sender instanceof Player)) {
                sender.sendMessage("Please provide a message");
                return false;
            }
            PlayerSession session = sessionManager.get(((Player) sender).getUniqueId());

            if (session.inStaffChatMode()) {
                message.send(sender, messages.staffChatStatus.replace("%status%", messages.disabled), messages.prefixStaffChat);
                session.setChatting(false);
            } else {
                message.send(sender, messages.staffChatStatus.replace("%status%", messages.enabled), messages.prefixStaffChat);
                session.setChatting(true);
            }
        }

        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

}