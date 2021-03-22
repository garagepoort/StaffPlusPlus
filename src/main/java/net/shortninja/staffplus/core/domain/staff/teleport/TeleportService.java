package net.shortninja.staffplus.core.domain.staff.teleport;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@IocBean
public class TeleportService {

    private static Map<Player, Deque<Location>> previousLocations = new HashMap<>();

    private final Options options;
    private final MessageCoordinator message;
    private final Messages messages;

    public TeleportService(Options options, MessageCoordinator message, Messages messages) {
        this.options = options;
        this.message = message;
        this.messages = messages;
    }

    public void teleportPlayerToLocation(CommandSender commandSender, Player targetPlayer, String locationId) {
        if (!options.locations.containsKey(locationId)) {
            throw new BusinessException("&CNo location found with name: " + locationId);
        }

        Location location = options.locations.get(locationId);
        addPreviousLocation(targetPlayer);
        targetPlayer.teleport(location);
        message.send(commandSender, "&6" + targetPlayer.getName() + " teleported to " + locationId, messages.prefixGeneral);
    }


    public void teleportSelf(Player targetPlayer, Location location) {
        addPreviousLocation(targetPlayer);
        targetPlayer.teleport(location);
        message.send(targetPlayer, "&6You have been teleported", messages.prefixGeneral);
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
