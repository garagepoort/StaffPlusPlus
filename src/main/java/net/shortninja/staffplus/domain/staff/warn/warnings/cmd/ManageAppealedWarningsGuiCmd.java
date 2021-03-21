package net.shortninja.staffplus.domain.staff.warn.warnings.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.warn.warnings.gui.ManageAppealedWarningsGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ManageAppealedWarningsGuiCmd extends AbstractCmd {

    public ManageAppealedWarningsGuiCmd(String name) {
        super(name, IocContainer.getOptions().manageWarningsConfiguration.getPermissionView());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if(!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        new ManageAppealedWarningsGui((Player) sender, player, "Manage Appealed Warnings", 0).show((Player) sender);
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
