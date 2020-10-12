package net.shortninja.staffplus.staff.ban.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.ban.BanService;
import net.shortninja.staffplus.staff.ban.BanUnit;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TempBanCmd extends AbstractCmd {

    private final BanService banService = IocContainer.getBanService();
    private PermissionHandler permissionHandler;
    private Options options;

    public TempBanCmd(String name) {
        super(name, IocContainer.getOptions().banConfiguration.getPermissionBanPlayer());
        permissionHandler = IocContainer.getPermissionHandler();
        options = IocContainer.getOptions();
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        int amount = Integer.parseInt(args[1]);
        String timeUnit = args[2];
        String reason = JavaUtils.compileWords(args, 3);

        banService.tempBan(sender, player, BanUnit.getTicks(timeUnit, amount), reason);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 4;
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
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.banConfiguration.getPermissionBanByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Stream.of("5", "10", "15", "20")
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }
        if (args.length == 3) {
            return Stream.of(
                BanUnit.YEAR.name(), BanUnit.MONTH.name(),
                BanUnit.WEEK.name(), BanUnit.DAY.name(),
                BanUnit.HOUR.name(), BanUnit.MINUTE.name())
                .filter(s -> args[2].isEmpty() || s.contains(args[2]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
