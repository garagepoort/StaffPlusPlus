package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class EChestView extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = IocContainer.getMessages();

    public EChestView(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be used by players");
            return true;
        }
        Player p = (Player) sender;
        if (!permission.has(p, options.permissionExamine)) {
            message.send(p, messages.noPermission, messages.prefixGeneral);
            return true;
        }
        if (args.length == 0) {
            message.send(p, messages.invalidArguments, messages.prefixGeneral);
            return true;
        }
        if (Bukkit.getServer().getPlayer(args[0]) != null) {
            p.openInventory(InventoryFactory.createEnderchestInventory(Bukkit.getServer().getPlayer(args[0])));
            StaffPlus.get().inventoryHandler.addVirtualUser(p.getUniqueId());
            return true;
        } else {
            if (!permission.has(p, options.permissionExamine)) {
                message.send(p, messages.noPermission, messages.prefixGeneral);
                return true;
            }
        }
        return true;
    }
}
