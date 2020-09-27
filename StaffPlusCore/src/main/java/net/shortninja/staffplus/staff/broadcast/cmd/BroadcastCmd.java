package net.shortninja.staffplus.staff.broadcast.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.staff.broadcast.BroadcastService;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BroadcastCmd extends AbstractCmd {

    private final BroadcastService broadcastService = IocContainer.getBroadcastService();

    public BroadcastCmd(String name) {
        super(name, IocContainer.getOptions().permissionBroadcast);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        String server = args[0];
        String message = JavaUtils.compileWords(args, 1);
        broadcastService.broadcastMessage(sender, message);
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
            suggestions.add("ALL");
            suggestions.add("CURRENT");
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return suggestions;
    }
}
