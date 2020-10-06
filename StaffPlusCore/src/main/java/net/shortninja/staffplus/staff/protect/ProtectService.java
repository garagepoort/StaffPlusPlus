package net.shortninja.staffplus.staff.protect;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.protect.database.ProtectedAreaRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

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

    public void createProtectedArea(int radius, String name, Player player) {
        Optional<ProtectedArea> existingArea = protectedAreaRepository.findByName(name);
        if (existingArea.isPresent()) {
            throw new BusinessException("A protected area with this name already exists. Please delete the existing area or choose another name.", messages.prefixProtect);
        }
        // TODO check max radius property
        Location location1 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + radius, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + radius);
        Location location2 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() - radius, player.getLocation().getBlockY(), player.getLocation().getBlockZ() - radius);

        ProtectedArea protectedArea = new ProtectedArea(name, location1, location2, player.getUniqueId());
        protectedAreas.add(protectedArea);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            protectedAreaRepository.addProtectedArea(player, protectedArea);
        });
        message.send(player, "Protected Area added", messages.prefixProtect);
    }

    public void deleteProtectedArea(Player player, String name) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findByName(name);
        if (!protectedArea.isPresent()) {
            throw new BusinessException("Cannot delete area. No area with name [" + name + "] found", messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(protectedArea.get().getId());
        Optional<ProtectedArea> first = protectedAreas.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            protectedAreas.remove(first.get());
            message.send(player, "Protected Area deleted", messages.prefixProtect);
        }
    }

    public void deleteProtectedArea(Player player, int id) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findById(id);
        if (!protectedArea.isPresent()) {
            throw new BusinessException("Cannot delete area. No area with id [" + id + "] found", messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(id);
        Optional<ProtectedArea> first = protectedAreas.stream().filter(p -> p.getId() == id).findFirst();
        if (first.isPresent()) {
            protectedAreas.remove(first.get());
            message.send(player, "Protected Area deleted", messages.prefixProtect);
        }
    }

    public List<ProtectedArea> getAllProtectedAreas() {
        return protectedAreas;
    }

    public List<ProtectedArea> getAllProtectedAreasPaginated(int offset, int amount) {
        return protectedAreaRepository.getProtectedAreasPaginated(offset, amount);
    }

    public ProtectedArea getById(int protectedAreaId) {
        return protectedAreaRepository.findById(protectedAreaId).orElseThrow(() -> new BusinessException("No area with id [" + protectedAreaId + "] found", messages.prefixProtect));
    }
}
