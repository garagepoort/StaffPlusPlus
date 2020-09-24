package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TraceCmd extends AbstractCmd {

    private static final String START = "start";
    private static final String STOP = "stop";
    private TraceService traceService;

    public TraceCmd(String name) {
        super(name, IocContainer.getOptions().permissionTrace);
        traceService = IocContainer.getTraceService();
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String command = args[0];
        if(command.equalsIgnoreCase(START)) {
            traceService.startTrace(sender, player);
            sender.sendMessage("-----------------------------------");
            sender.sendMessage("---------- Trace Started ----------");
            sender.sendMessage("-----------------------------------");
            return true;
        }
        if(command.equalsIgnoreCase(STOP)) {
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
        if(args.length > 1) {
            return Optional.ofNullable(args[1]);
        }
        return Optional.empty();
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase(START)) {
                return 2;
            }
        }
        return 1;
    }

    @Override
    protected boolean isDelayable() {
        return false;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return true;
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
        List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add(START);
            suggestions.add(STOP);
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            suggestions.addAll(onlinePLayers);
            return suggestions.stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
