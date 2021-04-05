package net.shortninja.staffplus.core.domain.staff.examine;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.chests.EnderChestService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "enderchest-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class EChestView extends AbstractCmd {

    private final EnderChestService enderChestService;
    private final PlayerManager playerManager;

    public EChestView(Messages messages, Options options, EnderChestService enderChestService, CommandService commandService, PlayerManager playerManager) {
        super(options.enderchestsConfiguration.getCommandOpenEnderChests(), messages, options, commandService);
        this.enderChestService = enderChestService;
        this.playerManager = playerManager;
        setDescription("Used to view a players ender chest");
        setUsage("[player]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer target) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be used by players");
            return true;
        }
        Player p = (Player) sender;
        enderChestService.openEnderChest(p, target);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
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
