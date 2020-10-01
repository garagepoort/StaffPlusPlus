package net.shortninja.staffplus.staff.protect;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.protect.database.ProtectedAreaRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class ProtectService {

    //Cached list for optimization
    private List<ProtectedArea> protectedAreas;

    private final ProtectedAreaRepository protectedAreaRepository;
    private final MessageCoordinator message;
    private final ModeCoordinator modeCoordinator;
    private final Messages messages;

    public ProtectService(ProtectedAreaRepository protectedAreaRepository, MessageCoordinator message, ModeCoordinator modeCoordinator, Messages messages) {
        this.protectedAreaRepository = protectedAreaRepository;
        this.message = message;
        this.modeCoordinator = modeCoordinator;
        this.messages = messages;
        this.protectedAreas = protectedAreaRepository.getProtectedAreas();
    }

    public boolean isLocationProtect(Player player, Location location) {
        if (modeCoordinator.isInMode(player.getUniqueId())) {
            return false;
        }

        boolean protectedArea = protectedAreas.stream().anyMatch(a -> a.isInArea(location));
        if (protectedArea) {
            message.send(player, "This area has been protected by a Staff Member", messages.prefixProtect);
        }
        return protectedArea;
    }

    public void protectArea(int radius, String name, Player player) {
        Location location1 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + radius, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + radius);
        Location location2 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() - radius, player.getLocation().getBlockY(), player.getLocation().getBlockZ() - radius);

        ProtectedArea protectedArea = new ProtectedArea(name, location1, location2, player.getUniqueId());
        protectedAreas.add(protectedArea);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            protectedAreaRepository.addProtectedArea(player, protectedArea);
        });
        message.send(player, "Protected Area added", messages.prefixProtect);
    }
}
