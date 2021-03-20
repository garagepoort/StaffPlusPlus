package net.shortninja.staffplus.domain.staff.teleport;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.config.Options;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TeleportService {

    private static Map<Player, Deque<Location>> previousLocations = new HashMap<>();

    private final Options options;

    public TeleportService(Options options) {
        this.options = options;
    }

    public void teleportPlayerToLocation(CommandSender commandSender, Player targetPlayer, String locationId) {
        if (!options.locations.containsKey(locationId)) {
            throw new BusinessException("&CNo location found with name: " + locationId);
        }

        Location location = options.locations.get(locationId);
        addPreviousLocation(targetPlayer);
        targetPlayer.teleport(location);
        IocContainer.getMessage().send(commandSender, "&6" + targetPlayer.getName() + " teleported to " + locationId, IocContainer.getMessages().prefixGeneral);
    }


    public void teleportSelf(Player targetPlayer, Location location) {
        addPreviousLocation(targetPlayer);
        targetPlayer.teleport(location);
        IocContainer.getMessage().send(targetPlayer, "&6You have been teleported", IocContainer.getMessages().prefixGeneral);
    }

    public void teleportToPlayer(Player sourcePlayer, Player targetPlayer) {
        Location location = targetPlayer.getLocation();
        location.setWorld(targetPlayer.getWorld());
        addPreviousLocation(sourcePlayer);
        sourcePlayer.teleport(location);
    }

    public void teleportPlayerBack(Player player) {
        if(!previousLocations.containsKey(player) || previousLocations.get(player).isEmpty()) {
            throw new BusinessException("&CUnable to teleport player back, no previous locations found");
        }
        Location previousLocation = previousLocations.get(player).pop();
        player.teleport(previousLocation);
    }

    private void addPreviousLocation(Player player) {
        if(!previousLocations.containsKey(player)) {
            previousLocations.put(player, new ArrayDeque<>());
        }
        previousLocations.get(player).push(player.getLocation());
    }
}
