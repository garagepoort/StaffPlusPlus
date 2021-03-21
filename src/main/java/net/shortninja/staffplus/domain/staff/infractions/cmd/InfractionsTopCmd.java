package net.shortninja.staffplus.domain.staff.infractions.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.domain.staff.infractions.gui.InfractionsTopGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InfractionsTopCmd extends AbstractCmd {
    public InfractionsTopCmd(String name) {
        super(name, IocContainer.getOptions().infractionsConfiguration.getPermissionViewInfractions());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        Player p = (Player) sender;
        if (args.length == 1) {
            if(!JavaUtils.isValidEnum(InfractionType.class, args[0])) {
                throw new BusinessException("&CInvalid infraction type provided");
            }
            InfractionType infractionType = InfractionType.valueOf(args[0]);
            new InfractionsTopGui(p,"Top infractions", 0, Arrays.asList(infractionType)).show(p);
        }else {
            new InfractionsTopGui(p,"Top infractions", 0).show(p);
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

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Arrays.stream(InfractionType.values()).map(Enum::name).collect(Collectors.toList());
    }
}
