package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
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
        if (!vanishConfiguration.enabled) {
            return;
        }

        if(vanishCache.getOrDefault(player.getUniqueId(), VanishType.NONE) == vanishType) {
            return;
        }

        PlayerSettings settings = playerSettingsRepository.get(player);
        settings.setVanishType(vanishType);
        playerSettingsRepository.save(settings);
        vanishCache.put(player.getUniqueId(), vanishType);
        sendEvent(new VanishOnEvent(vanishType, player));
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

        sendEvent(new VanishOffEvent(vanishType, player));
    }

    public void clearCache(Player player) {
        vanishCache.remove(player.getUniqueId());
    }

    public boolean isVanished(Player player) {
        PlayerSettings user = playerSettingsRepository.get(player);
        return user.getVanishType() != VanishType.NONE;
    }
}
