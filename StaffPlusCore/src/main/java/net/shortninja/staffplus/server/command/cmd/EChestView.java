package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.staff.chests.ChestGUI;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EChestView extends AbstractCmd {

    public EChestView(String name) {
        super(name, IocContainer.getOptions().examineConfiguration.getPermissionExamine());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer target) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be used by players");
            return true;
        }
        Player p = (Player) sender;
        if(target.isOnline()) {
            new ChestGUI(p, target, target.getPlayer().getEnderChest(), InventoryType.ENDER_CHEST);
        } else {
            if(!options.enderOfflineChestEnabled) {
                throw new PlayerOfflineException();
            }

            Inventory offlineEnderchest = InventoryFactory.loadEnderchestOffline(p, target);
            new ChestGUI(p, target, offlineEnderchest, InventoryType.ENDER_CHEST);
        }
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
