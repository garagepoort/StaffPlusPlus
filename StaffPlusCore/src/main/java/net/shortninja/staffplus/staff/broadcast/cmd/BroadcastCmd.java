package net.shortninja.staffplus.staff.broadcast.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.staff.broadcast.BroadcastService;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastSelector;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BroadcastCmd extends AbstractCmd {

    private final BroadcastService broadcastService = IocContainer.getBroadcastService();
    private final BroadcastConfiguration broadcastConfiguration = IocContainer.getOptions().broadcastConfiguration;

    public BroadcastCmd(String name) {
        super(name, IocContainer.getOptions().permissionBroadcast);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String serverSelector = args[0];
        String message = JavaUtils.compileWords(args, 1);

        if (serverSelector.equalsIgnoreCase(BroadcastSelector.ALL.name())) {
            broadcastService.broadcastToAll(sender, message);
            return true;
        }
        if (serverSelector.equalsIgnoreCase(BroadcastSelector.CURRENT.name())) {
            broadcastService.broadcastToCurrent(message);
            return true;
        }
        String[] servers = serverSelector.split(";");
        broadcastService.broadcastToSpecific(sender, Arrays.asList(servers), message);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
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
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            if(broadcastConfiguration.sendToCurrent()){
                suggestions.add("CURRENT");
            }
            if(broadcastConfiguration.sendToAll() || broadcastConfiguration.multipleServers()) {
                suggestions.add("ALL");
            }
            if(broadcastConfiguration.multipleServers()) {
                suggestions.addAll(broadcastConfiguration.getEnabledServers());
            }
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return suggestions;
    }
}
