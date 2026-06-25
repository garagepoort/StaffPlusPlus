package net.shortninja.staffplus.core.domain.staff.protect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.messages.Messages;
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

    public ProtectService(ProtectedAreaRepository protectedAreaRepository,
                          Messages messages,
                          ProtectConfiguration protectConfiguration,
                          PlayerSettingsRepository playerSettingsRepository) {
        this.protectedAreaRepository = protectedAreaRepository;
        this.messages = messages;
        this.protectConfiguration = protectConfiguration;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    public boolean isLocationProtected(Player player, Location location) {
        if (playerSettingsRepository.get(player).isInStaffMode()) {
            return false;
        }

        boolean protectedArea = getAllProtectedAreas().stream().anyMatch(a -> a.isInArea(location));
        if (protectedArea) {
            messages.sendTranslation(player, "protect-area-protected", messages.prefixProtect);
        }
        return protectedArea;
    }

    public void createProtectedArea(int size, String name, Player player, World world, Location location) {
        Optional<ProtectedArea> existingArea = protectedAreaRepository.findByName(name);
        if (existingArea.isPresent()) {
            throw new BusinessException(messages.get("protect-error-area-exists"), messages.prefixProtect);
        }
        if (size > protectConfiguration.areaMaxSize) {
            throw new BusinessException(messages.get("protect-error-area-too-big", "%maxSize%", Integer.toString(protectConfiguration.areaMaxSize)), messages.prefixProtect);
        }

        int half = size / 2;
        int correction = size % 2 == 0 ? 1 : 0;
        Location location1 = new Location(world, location.getBlockX() + half, location.getBlockY(), location.getBlockZ() + half);
        Location location2 = new Location(world, location.getBlockX() - half + correction, location.getBlockY(), location.getBlockZ() - half + correction);

        ProtectedArea protectedArea = new ProtectedArea(name, location1, location2, player.getUniqueId());
        getAllProtectedAreas().add(protectedArea);
        protectedAreaRepository.addProtectedArea(player, protectedArea);
        messages.sendTranslation(player, "protect-area-added", messages.prefixProtect);
    }

    public void deleteProtectedArea(Player player, String name) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findByName(name);
        if (!protectedArea.isPresent()) {
            throw new BusinessException(messages.get("protect-error-area-name-not-found", "%name%", name), messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(protectedArea.get().getId());
        Optional<ProtectedArea> first = getAllProtectedAreas().stream().filter(p -> p.getName().equals(name)).findFirst();
        if (first.isPresent()) {
            getAllProtectedAreas().remove(first.get());
            messages.sendTranslation(player, "protect-area-deleted", messages.prefixProtect);
        }
    }

    public void deleteProtectedArea(Player player, int id) {
        Optional<ProtectedArea> protectedArea = protectedAreaRepository.findById(id);
        if (!protectedArea.isPresent()) {
            throw new BusinessException(messages.get("protect-error-area-id-not-found", "%id%", Integer.toString(id)), messages.prefixProtect);
        }
        protectedAreaRepository.deleteProtectedArea(id);
        Optional<ProtectedArea> first = getAllProtectedAreas().stream().filter(p -> p.getName().equals(protectedArea.get().getName())).findFirst();
        if (first.isPresent()) {
            getAllProtectedAreas().remove(first.get());
            messages.sendTranslation(player, "protect-area-deleted", messages.prefixProtect);
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
        return protectedAreaRepository.findById(protectedAreaId).orElseThrow(() -> new BusinessException(messages.get("protect-error-area-id-not-found", "%id%", Integer.toString(protectedAreaId)), messages.prefixProtect));
    }
}
