package net.shortninja.staffplus.core.domain.staff.teleport;

import be.garagepoort.mcioc.IocBean;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.NBTList;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.teleport.config.TeleportConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@IocBean
public class TeleportService {

    private static final Map<Player, Deque<Location>> previousLocations = new HashMap<>();

    private final Options options;
    private final Messages messages;
    private final TeleportConfiguration teleportConfiguration;

    public TeleportService(Options options, Messages messages, TeleportConfiguration teleportConfiguration) {
        this.options = options;
        this.messages = messages;
        this.teleportConfiguration = teleportConfiguration;
    }

    public void teleportPlayerToLocation(CommandSender commandSender, Player targetPlayer, String locationId) {
        if (!teleportConfiguration.locations.containsKey(locationId)) {
            throw new BusinessException(messages.get("teleport-error-location-not-found", "%location%", locationId), messages.prefixGeneral);
        }

        Location location = teleportConfiguration.locations.get(locationId);
        addPreviousLocation(targetPlayer);
        targetPlayer.teleport(location);
        messages.sendTranslation(commandSender, "teleport-player-to-location", messages.prefixGeneral, "%player%", targetPlayer.getName(), "%location%", locationId);
    }

    public void teleportSelf(Player targetPlayer, Location location) {
        addPreviousLocation(targetPlayer);
        targetPlayer.teleport(location);
        messages.sendTranslation(targetPlayer, "teleport-teleported", messages.prefixGeneral);
    }

    public void teleportToPlayer(Player sourcePlayer, Player targetPlayer) {
        Location location = targetPlayer.getLocation();
        location.setWorld(targetPlayer.getWorld());
        addPreviousLocation(sourcePlayer);
        sourcePlayer.teleport(location);
    }

    public void teleportToPlayer(Player sourcePlayer, SppPlayer targetPlayer) {
        Location location = targetPlayer.isOnline() ? targetPlayer.getPlayer().getLocation() : getLocationOfflinePlayer(targetPlayer);
        addPreviousLocation(sourcePlayer);
        sourcePlayer.teleport(location);
    }

    public void teleportPlayerBack(Player player) {
        if (!previousLocations.containsKey(player) || previousLocations.get(player).isEmpty()) {
            throw new BusinessException(messages.get("teleport-error-no-previous-location"), messages.prefixGeneral);
        }
        Location previousLocation = previousLocations.get(player).pop();
        player.teleport(previousLocation);
    }

    private void addPreviousLocation(Player player) {
        if (!previousLocations.containsKey(player)) {
            previousLocations.put(player, new ArrayDeque<>());
        }
        previousLocations.get(player).push(player.getLocation());
    }

    private Location getLocationOfflinePlayer(SppPlayer sppPlayer) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + options.mainWorld + File.separator + "playerdata" + File.separator + sppPlayer.getId() + ".dat";
            NBTFile file = new NBTFile(new File(filename));
            long uuidLeast = file.getLong("WorldUUIDLeast");
            long uuidMost = file.getLong("WorldUUIDMost");
            NBTList<Double> positions = file.getDoubleList("Pos");

            World world = Bukkit.getWorlds().stream()
                .filter(w -> w.getUID().getLeastSignificantBits() == uuidLeast && w.getUID().getMostSignificantBits() == uuidMost).findFirst()
                .orElseThrow(() -> new BusinessException(messages.get("teleport-error-offline-location", "%player%", sppPlayer.getUsername()), messages.prefixGeneral));

            return new Location(world, positions.get(0), positions.get(1), positions.get(2));
        } catch (IOException e) {
            throw new BusinessException(messages.get("teleport-error-offline-location", "%player%", sppPlayer.getUsername()), messages.prefixGeneral);
        }
    }
}
