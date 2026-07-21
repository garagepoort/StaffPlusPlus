package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import de.myzelyam.api.vanish.VanishAPI;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class VanishService {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final VanishConfiguration vanishConfiguration;
    private static final Map<UUID, VanishType> vanishCache = new HashMap<>();

    public VanishService(PlayerSettingsRepository playerSettingsRepository,
                         VanishConfiguration vanishConfiguration) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.vanishConfiguration = vanishConfiguration;
    }

    public void addVanish(Player player, VanishType vanishType) {
        addVanish(player, vanishType, false);
    }

    public void addVanish(Player player, VanishType vanishType, boolean onJoin) {
        if (!vanishConfiguration.enabled) {
            return;
        }

        if (vanishCache.getOrDefault(player.getUniqueId(), VanishType.NONE) == vanishType) {
            return;
        }

        PlayerSettings settings = playerSettingsRepository.get(player);
        settings.setVanishType(vanishType);
        playerSettingsRepository.save(settings);
        vanishCache.put(player.getUniqueId(), vanishType);

        // Sync with SuperVanish: hide for TOTAL and PLAYER types
        if (vanishType == VanishType.TOTAL || vanishType == VanishType.PLAYER) {
            // Ensure the player is not already vanished before hiding
            if (!VanishAPI.isInvisible(player)) {
                VanishAPI.hidePlayer(player);
            }
        }

        sendEvent(new VanishOnEvent(vanishType, player, onJoin));
    }

    public void removeVanish(Player player) {
        if (!vanishConfiguration.enabled) {
            return;
        }
        PlayerSettings session = playerSettingsRepository.get(player);
        VanishType vanishType = session.getVanishType();
        session.setVanishType(VanishType.NONE);
        vanishCache.put(player.getUniqueId(), VanishType.NONE);
        playerSettingsRepository.save(session);

        // Sync with SuperVanish: show the player
        if (VanishAPI.isInvisible(player)) {
            VanishAPI.showPlayer(player);
        }

        sendEvent(new VanishOffEvent(vanishType, player));
    }

    public void clearCache(Player player) {
        vanishCache.remove(player.getUniqueId());
    }

    public boolean isVanished(Player player) {
        // Use SuperVanish as the source of truth for TOTAL and PLAYER vanish types
        if (VanishAPI.isInvisible(player)) {
            return true;
        }
        // Fall back to PlayerSettings for LIST vanish type (which SuperVanish doesn't manage)
        PlayerSettings user = playerSettingsRepository.get(player);
        return user.getVanishType() != VanishType.NONE;
    }
}
