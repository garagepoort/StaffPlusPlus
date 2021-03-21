package net.shortninja.staffplus.domain.staff.mode.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.mode.handler.GadgetHandler;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class CpsCmd extends AbstractCmd {
    private final GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;

    public CpsCmd(String name) {
        super(name, IocContainer.getOptions().permissionCps);
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        gadgetHandler.onCps(sender, targetPlayer.getPlayer());
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }
}