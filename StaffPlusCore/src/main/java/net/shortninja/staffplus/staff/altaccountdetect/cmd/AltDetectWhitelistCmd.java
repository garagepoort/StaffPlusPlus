package net.shortninja.staffplus.staff.altaccountdetect.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AltDetectWhitelistCmd extends AbstractCmd {
    public AltDetectWhitelistCmd(String name) {
        super(name, IocContainer.getOptions().altDetectConfiguration.getWhitelistPermission());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {

        String action = args[0];
        SppPlayer player1 = playerManager.getOnOrOfflinePlayer(args[1]).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
        SppPlayer player2 = playerManager.getOnOrOfflinePlayer(args[2]).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

        if (action.equalsIgnoreCase("add")) {
            IocContainer.getAltDetectionService().addToWhitelist(sender, player1, player2);
            return true;
        }

        if (action.equalsIgnoreCase("remove")) {
            IocContainer.getAltDetectionService().removeFromWhitelist(sender, player1, player2);
            return true;
        }
        throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 3;
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
        List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("add");
            suggestions.add("remove");
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length >= 1) {
            suggestions.addAll(onlinePLayers);
            suggestions.addAll(offlinePlayers);
            return suggestions.stream()
                .filter(s -> args[args.length - 1].isEmpty() || s.contains(args[args.length - 1]))
                .collect(Collectors.toList());
        }

        return super.tabComplete(sender, alias, args);
    }
}
