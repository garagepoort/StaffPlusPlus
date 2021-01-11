package net.shortninja.staffplus.staff.ban.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.staff.ban.BanService;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnbanCmd extends AbstractCmd {

    private final BanService banService = IocContainer.getBanService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();

    public UnbanCmd(String name) {
        super(name, IocContainer.getOptions().banConfiguration.getPermissionUnbanPlayer());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String reason = JavaUtils.compileWords(args, 1);

        banService.unban(sender, player, reason);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
