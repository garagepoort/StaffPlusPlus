package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StaffPlusCmd extends AbstractCmd {

    public StaffPlusCmd(String name) {
        super(name, IocContainer.getOptions().permissionStaff);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (args[0].equalsIgnoreCase("reload")) {
            IocContainer.getMessage().sendConsoleMessage("This feature is disabled until we have implemented a robust way of reloading", true);
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
        if (args.length == 0) {
            return Collections.singletonList("reload");
        }

        return super.tabComplete(sender, alias, args);
    }
}
