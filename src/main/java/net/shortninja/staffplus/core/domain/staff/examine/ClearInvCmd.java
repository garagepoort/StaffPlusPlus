package net.shortninja.staffplus.core.domain.staff.examine;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.TELEPORT;

public class ClearInvCmd extends AbstractCmd {

    public ClearInvCmd(String name) {
        super(name, IocContainer.get(Options.class).permissionClearInv);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        List<String> arguments = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));

        JavaUtils.clearInventory(targetPlayer.getPlayer());
        sender.sendMessage(targetPlayer.getPlayer().getName() + "'s inventory has been cleared");
        return true;
    }

    @Override
    protected List<ArgumentType> getPostExecutionSppArguments() {
        return Arrays.asList(TELEPORT, HEALTH);
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return ONLINE;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permission.has(player, options.permissionClearInvBypass);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }

        return getSppArgumentsSuggestions(sender, args);
    }
}
