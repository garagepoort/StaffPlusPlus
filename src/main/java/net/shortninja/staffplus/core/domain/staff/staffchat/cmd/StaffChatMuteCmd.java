package net.shortninja.staffplus.core.domain.staff.staffchat.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean(conditionalOnProperty = "staff-chat-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class StaffChatMuteCmd extends AbstractCmd {
    private final SessionManagerImpl sessionManager;

    public StaffChatMuteCmd(Messages messages, Options options, SessionManagerImpl sessionManager, CommandService commandService) {
        super(options.staffChatConfiguration.getCommandStaffChatMute(), messages, options, commandService);
        this.sessionManager = sessionManager;
        setDescription("Mutes all staff chat. You can still send messages to staff chat but you won't see anything.");
        setPermission(options.staffChatConfiguration.getPermissionStaffChatMute());
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        PlayerSession session = sessionManager.get(((Player) sender).getUniqueId());

        if (session.isStaffChatMuted()) {
            messages.send(sender, messages.staffChatUnmuted, messages.prefixStaffChat);
        } else {
            messages.send(sender, messages.staffChatMuted, messages.prefixStaffChat);
        }

        session.setStaffChatMuted(!session.isStaffChatMuted());
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