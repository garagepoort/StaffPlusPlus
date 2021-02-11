package net.shortninja.staffplus.staff.warn.warnings.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.staff.warn.warnings.gui.MyWarningsGui;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MyWarningsCmd extends AbstractCmd {

    private final WarnService warnService = IocContainer.getWarnService();

    public MyWarningsCmd(String name) {
        super(name, IocContainer.getOptions().warningConfiguration.getMyWarningsPermission());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        new MyWarningsGui((Player) sender, "My Warnings", 0).show((Player) sender);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), ()-> warnService.markWarningsRead(((Player) sender).getUniqueId()));
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
