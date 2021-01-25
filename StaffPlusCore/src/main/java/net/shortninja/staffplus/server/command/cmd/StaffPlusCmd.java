package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StaffPlusCmd extends AbstractCmd {

    private MessageCoordinator message = IocContainer.getMessage();

    public StaffPlusCmd(String name) {
        super(name, IocContainer.getOptions().permissionStaff);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (args[0].equalsIgnoreCase("reload")) {
            IocContainer.getOptions().reload();
            IocContainer.getMessages().reload();
            StaffPlus.get().cmdHandler.reload();
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
