package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class EChestView extends AbstractCmd {

    public EChestView(String name) {
        super(name, IocContainer.getOptions().permissionExamine);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be used by players");
            return true;
        }
        Player p = (Player) sender;
        p.openInventory(InventoryFactory.createEnderchestInventory(player.getPlayer()));
        StaffPlus.get().inventoryHandler.addVirtualUser(p.getUniqueId());
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }

}
