package net.shortninja.staffplus.core.domain.staff.tracing;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TraceCmd extends AbstractCmd {

    private static final String START = "start";
    private static final String STOP = "stop";
    private TraceService traceService;

    public TraceCmd(String name) {
        super(name, StaffPlus.get().iocContainer.get(Options.class).permissionTrace);
        traceService = StaffPlus.get().iocContainer.get(TraceService.class);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String command = args[0];
        if (command.equalsIgnoreCase(START)) {
            traceService.startTrace(sender, player);
            sender.sendMessage("-----------------------------------");
            sender.sendMessage("---------- Trace Started ----------");
            sender.sendMessage("-----------------------------------");
            return true;
        }
        if (command.equalsIgnoreCase(STOP)) {
            traceService.stopTrace(sender);
            sender.sendMessage("-----------------------------------");
            sender.sendMessage("---------- Trace Stopped ----------");
            sender.sendMessage("-----------------------------------");
            return true;
        }
        return true;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length > 1) {
            return Optional.ofNullable(args[1]);
        }
        return Optional.empty();
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(START)) {
                return 2;
            }
        }
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected boolean canBypass(Player player) {
        return player.hasPermission(options.permissionTraceBypass);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of(START, STOP)
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            return onlinePLayers.stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
