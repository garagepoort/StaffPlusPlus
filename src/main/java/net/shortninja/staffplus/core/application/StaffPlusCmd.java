package net.shortninja.staffplus.core.application;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CmdHandler;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StaffPlusCmd extends AbstractCmd {

    private MessageCoordinator message = StaffPlus.get().iocContainer.get(MessageCoordinator.class);

    public StaffPlusCmd(String name) {
        super(name, StaffPlus.get().iocContainer.get(Options.class).permissionStaff);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (args[0].equalsIgnoreCase("reload")) {
            StaffPlus.get().iocContainer.get(Options.class).reload();
            StaffPlus.get().iocContainer.get(Messages.class).reload();
            StaffPlus.get().iocContainer.get(CmdHandler.class).reload();
            sender.sendMessage("");
            message.send(sender, "Configuration has been reloaded", messages.prefixGeneral);
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1 && !args[0].equalsIgnoreCase("reload")) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }
}
