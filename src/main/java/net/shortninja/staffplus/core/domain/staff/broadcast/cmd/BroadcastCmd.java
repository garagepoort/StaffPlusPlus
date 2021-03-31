package net.shortninja.staffplus.core.domain.staff.broadcast.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.broadcast.BroadcastService;
import net.shortninja.staffplus.core.domain.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.core.domain.staff.broadcast.config.BroadcastSelector;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(SppCommand.class)
public class BroadcastCmd extends AbstractCmd {

    private final BroadcastService broadcastService;
    private final BroadcastConfiguration broadcastConfiguration;

    public BroadcastCmd(Messages messages, Options options, BroadcastService broadcastService, CommandService commandService) {
        super(options.commandBroadcast, messages, options, commandService);
        this.broadcastService = broadcastService;
        this.broadcastConfiguration = options.broadcastConfiguration;
        setPermission(options.permissionBroadcast);
        setDescription("Broadcast messages to all players (over all servers)");
        setUsage("[server] [message]");
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
