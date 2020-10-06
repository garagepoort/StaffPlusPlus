package net.shortninja.staffplus.staff.teleport;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportService {

    private static TeleportService instance;
    private final Options options;

    public TeleportService(Options options) {
        this.options = options;
    }

    public void teleportPlayer(CommandSender commandSender, Player targetPlayer, String locationId) {
        if (!options.locations.containsKey(locationId)) {
            commandSender.sendMessage("No location found with name: " + locationId);
            return;
        }

        Location location = options.locations.get(locationId);
        targetPlayer.teleport(location);
        IocContainer.getMessage().send(commandSender, targetPlayer.getName() + " teleported to " + locationId, IocContainer.getMessages().prefixGeneral);
    }


    public void teleportSelf(Player targetPlayer, Location location) {
        targetPlayer.teleport(location);
        IocContainer.getMessage().send(targetPlayer, "You have been teleported", IocContainer.getMessages().prefixGeneral);
    }

    public void teleportToPlayer(Player sourcePlayer, Player targetPlayer) {
        Location location = targetPlayer.getLocation();
        location.setWorld(targetPlayer.getWorld());
        sourcePlayer.teleport(location);
    }
}
