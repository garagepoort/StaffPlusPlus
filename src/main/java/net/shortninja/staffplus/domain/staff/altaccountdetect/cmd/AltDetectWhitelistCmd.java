package net.shortninja.staffplus.domain.staff.altaccountdetect.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.altaccountdetect.AltDetectWhitelistedItem;
import net.shortninja.staffplus.domain.staff.altaccountdetect.AltDetectionService;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AltDetectWhitelistCmd extends AbstractCmd {

    private final AltDetectionService altDetectionService = IocContainer.getAltDetectionService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final MessageCoordinator message = IocContainer.getMessage();

    public AltDetectWhitelistCmd(String name) {
        super(name, IocContainer.getOptions().altDetectConfiguration.getWhitelistPermission());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {

        String action = args[0];

        if (action.equalsIgnoreCase("add")) {
            SppPlayer player1 = playerManager.getOnOrOfflinePlayer(args[1]).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
            SppPlayer player2 = playerManager.getOnOrOfflinePlayer(args[2]).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
            altDetectionService.addToWhitelist(sender, player1, player2);
            return true;
        }

        if (action.equalsIgnoreCase("remove")) {
            SppPlayer player1 = playerManager.getOnOrOfflinePlayer(args[1]).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
            SppPlayer player2 = playerManager.getOnOrOfflinePlayer(args[2]).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
            altDetectionService.removeFromWhitelist(sender, player1, player2);
            return true;
        }

        if (action.equalsIgnoreCase("list")) {
            int page = args.length > 1 ? Integer.parseInt(args[1]) : 1;
            int offset = (page - 1) * 20;
            List<AltDetectWhitelistedItem> whitelistedItems = altDetectionService.getWhitelistedItems(sender, offset, 20);
            int counter = offset + 1;
            for (AltDetectWhitelistedItem whitelistedItem : whitelistedItems) {
                String whitelistPlayer1 = getPlayerName(whitelistedItem.getPlayerUuid1());
                String whitelistPlayer2 = getPlayerName(whitelistedItem.getPlayerUuid2());
                message.send(sender, String.format("&B#%s: %s - %s", counter, whitelistPlayer1, whitelistPlayer2), messages.prefixGeneral);
                counter++;
            }
            if (whitelistedItems.isEmpty()) {
                message.send(sender, String.format("&6No items to display", page), messages.prefixGeneral);
            } else {
                message.send(sender, String.format("&6Showing page #%s", page), messages.prefixGeneral);
            }

            return true;
        }
        throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
    }

    private String getPlayerName(UUID uuid) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(uuid);
        if (player.isPresent()) {
            return player.get().getUsername();
        }
        return "Unknown player with uuid [" + uuid + "]";
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("list")) {
            return 1;
        }
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
        if (args.length == 1) {
            return Stream.of("add", "remove", "list")
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length >= 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[args.length - 1].isEmpty() || s.contains(args[args.length - 1]))
                .collect(Collectors.toList());
        }

        return super.tabComplete(sender, alias, args);
    }
}
