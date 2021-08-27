package net.shortninja.staffplus.core.domain.staff.protect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.protect.config.ProtectConfiguration;
import net.shortninja.staffplus.core.domain.staff.protect.database.ProtectedAreaRepository;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@IocBean
public class ProtectService {

    private final ProtectConfiguration protectConfiguration;
    //Cached list for optimization
    private List<ProtectedArea> protectedAreas;

    private final ProtectedAreaRepository protectedAreaRepository;

    private final Messages messages;
    private final PlayerSettingsRepository playerSettingsRepository;

    public ProtectService(ProtectedAreaRepository protectedAreaRepository, Messages messages, Options options, PlayerSettingsRepository playerSettingsRepository) {
        this.protectedAreaRepository = protectedAreaRepository;
        this.messages = messages;
        this.protectConfiguration = options.protectConfiguration;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    public boolean isLocationProtected(Player player, Location location) {
        if (playerSettingsRepository.get(player).isInStaffMode()) {
            return false;
        }

        boolean protectedArea = getAllProtectedAreas().stream().anyMatch(a -> a.isInArea(location));
        if (protectedArea) {
            messages.send(player, "&7This area has been protected by a Staff Member", messages.prefixProtect);
        }
        return protectedArea;
    }

    public void createProtectedArea(int size, String name, Player player, World world, Location location) {
        Optional<ProtectedArea> existingArea = protectedAreaRepository.findByName(name);
        if (existingArea.isPresent()) {
            throw new BusinessException("&bA protected area with this name already exists. Please delete the existing area or choose another name.", messages.prefixProtect);
        }
        if (size > protectConfiguration.getAreaMaxSize()) {
            throw new BusinessException("&bCannot create area, size is too big. Max size [" + protectConfiguration.getAreaMaxSize() + "]", messages.prefixProtect);
        }

        int half = size / 2;
        int correction = size % 2 == 0 ? 1 : 0;
        Location location1 = new Location(world, location.getBlockX() + half, location.getBlockY(), location.getBlockZ() + half);
        Location location2 = new Location(world, location.getBlockX() - half + correction, location.getBlockY(), location.getBlockZ() - half + correction);

        ProtectedArea protectedArea = new ProtectedArea(name, location1, location2, player.getUniqueId());
        getAllProtectedAreas().add(protectedArea);
        protectedAreaRepository.addProtectedArea(player, protectedArea);
        messages.send(player, "&bProtected Area added", messages.prefixProtect);
    }

    public void deleteProtectedArea(Player player, String name) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findByName(name);
        if (!protectedArea.isPresent()) {
            throw new BusinessException("&bCannot delete area. No area with name [" + name + "] found", messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(protectedArea.get().getId());
        Optional<ProtectedArea> first = getAllProtectedAreas().stream().filter(p -> p.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            getAllProtectedAreas().remove(first.get());
            messages.send(player, "&bProtected Area deleted", messages.prefixProtect);
        }
    }

    public void deleteProtectedArea(Player player, int id) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findById(id);
        if (!protectedArea.isPresent()) {
            throw new BusinessException("&bCannot delete area. No area with id [" + id + "] found", messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(id);
        Optional<ProtectedArea> first = getAllProtectedAreas().stream().filter(p -> p.getName().equals(protectedArea.get().getName())).findFirst();
        if (first.isPresent()) {
            getAllProtectedAreas().remove(first.get());
            messages.send(player, "&bProtected Area deleted", messages.prefixProtect);
        }
    }

    public List<ProtectedArea> getAllProtectedAreas() {
        if (protectedAreas == null) {
            this.protectedAreas = Collections.synchronizedList(protectedAreaRepository.getProtectedAreas());
        }
        return protectedAreas;
    }

    public List<ProtectedArea> getAllProtectedAreasPaginated(int offset, int amount) {
        return protectedAreaRepository.getProtectedAreasPaginated(offset, amount);
    }

    public ProtectedArea getById(int protectedAreaId) {
        return protectedAreaRepository.findById(protectedAreaId).orElseThrow(() -> new BusinessException("&CNo area with id [" + protectedAreaId + "] found", messages.prefixProtect));
    }
}
