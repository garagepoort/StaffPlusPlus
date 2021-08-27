package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class VanishServiceImpl {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final VanishConfiguration vanishConfiguration;

    public VanishServiceImpl(PlayerSettingsRepository playerSettingsRepository,
                             VanishConfiguration vanishConfiguration) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.vanishConfiguration = vanishConfiguration;
    }

    public void addVanish(Player player, VanishType vanishType) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }

        PlayerSettings settings = playerSettingsRepository.get(player);
        settings.setVanishType(vanishType);
        playerSettingsRepository.save(settings);
        sendEvent(new VanishOnEvent(vanishType, player));
    }

    public void removeVanish(Player player) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        PlayerSettings session = playerSettingsRepository.get(player);
        VanishType vanishType = session.getVanishType();
        session.setVanishType(VanishType.NONE);
        playerSettingsRepository.save(session);

        sendEvent(new VanishOffEvent(vanishType, player));
    }

    public boolean isVanished(Player player) {
        PlayerSettings user = playerSettingsRepository.get(player);
        return user.getVanishType() != VanishType.NONE;
    }
}
