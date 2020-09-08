package net.shortninja.staffplus.teleport;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportService {

    private static TeleportService instance;
    private final Options options = StaffPlus.get().options;

    private TeleportService() {
    }

    public static TeleportService getInstance() {
        if (instance == null) {
            instance = new TeleportService();
        }
        return instance;
    }

    public void teleportPlayer(CommandSender commandSender, Player targetPlayer, String locationId) {
        if (!options.locations.containsKey(locationId)) {
            commandSender.sendMessage("No location found with name: " + locationId);
            return;
        }

        Location location = options.locations.get(locationId);
        location.setWorld(targetPlayer.getWorld());
        targetPlayer.teleport(location);
    }
}
