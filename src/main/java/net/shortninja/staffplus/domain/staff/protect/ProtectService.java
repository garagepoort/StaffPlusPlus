package net.shortninja.staffplus.domain.staff.protect;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.domain.staff.protect.config.ProtectConfiguration;
import net.shortninja.staffplus.domain.staff.protect.database.ProtectedAreaRepository;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ProtectService {

    private final ProtectConfiguration protectConfiguration;
    //Cached list for optimization
    private List<ProtectedArea> protectedAreas;

    private final ProtectedAreaRepository protectedAreaRepository;
    private final MessageCoordinator message;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;

    public ProtectService(ProtectedAreaRepository protectedAreaRepository, MessageCoordinator message, Messages messages, Options options, SessionManagerImpl sessionManager) {
        this.protectedAreaRepository = protectedAreaRepository;
        this.message = message;
        this.messages = messages;
        this.protectedAreas = protectedAreaRepository.getProtectedAreas();
        this.protectConfiguration = options.protectConfiguration;
        this.sessionManager = sessionManager;
    }

    public boolean isLocationProtect(Player player, Location location) {
        if (sessionManager.get(player.getUniqueId()).isInStaffMode()) {
            return false;
        }

        boolean protectedArea = protectedAreas.stream().anyMatch(a -> a.isInArea(location));
        if (protectedArea) {
            message.send(player, "&7This area has been protected by a Staff Member", messages.prefixProtect);
        }
        return protectedArea;
    }

    public void createProtectedArea(int size, String name, Player player) {
        Optional<ProtectedArea> existingArea = protectedAreaRepository.findByName(name);
        if (existingArea.isPresent()) {
            throw new BusinessException("&bA protected area with this name already exists. Please delete the existing area or choose another name.", messages.prefixProtect);
        }
        if (size > protectConfiguration.getAreaMaxSize()) {
            throw new BusinessException("&bCannot create area, size is too big. Max size [" + protectConfiguration.getAreaMaxSize() + "]", messages.prefixProtect);
        }

        int half = size/2;
        int correction = size%2 == 0 ? 1 : 0;
        Location location1 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + half, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + half);
        Location location2 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() - half + correction, player.getLocation().getBlockY(), player.getLocation().getBlockZ() - half + correction);

        ProtectedArea protectedArea = new ProtectedArea(name, location1, location2, player.getUniqueId());
        protectedAreas.add(protectedArea);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            protectedAreaRepository.addProtectedArea(player, protectedArea);
        });
        message.send(player, "&bProtected Area added", messages.prefixProtect);
    }

    public void deleteProtectedArea(Player player, String name) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findByName(name);
        if (!protectedArea.isPresent()) {
            throw new BusinessException("&bCannot delete area. No area with name [" + name + "] found", messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(protectedArea.get().getId());
        Optional<ProtectedArea> first = protectedAreas.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            protectedAreas.remove(first.get());
            message.send(player, "&bProtected Area deleted", messages.prefixProtect);
        }
    }

    public void deleteProtectedArea(Player player, int id) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findById(id);
        if (!protectedArea.isPresent()) {
            throw new BusinessException("&bCannot delete area. No area with id [" + id + "] found", messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(id);
        Optional<ProtectedArea> first = protectedAreas.stream().filter(p -> p.getName().equals(protectedArea.get().getName())).findFirst();
        if (first.isPresent()) {
            protectedAreas.remove(first.get());
            message.send(player, "&bProtected Area deleted", messages.prefixProtect);
        }
    }

    public List<ProtectedArea> getAllProtectedAreas() {
        return protectedAreas;
    }

    public List<ProtectedArea> getAllProtectedAreasPaginated(int offset, int amount) {
        return protectedAreaRepository.getProtectedAreasPaginated(offset, amount);
    }

    public ProtectedArea getById(int protectedAreaId) {
        return protectedAreaRepository.findById(protectedAreaId).orElseThrow(() -> new BusinessException("&CNo area with id [" + protectedAreaId + "] found", messages.prefixProtect));
    }
}
