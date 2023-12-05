package net.shortninja.staffplus.core.domain.staff.nightvision;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.nightvision.NightVisionOffEvent;
import net.shortninja.staffplusplus.nightvision.NightVisionOnEvent;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class NightVisionService implements net.shortninja.staffplusplus.nightvision.NightVisionService {

    private final PlayerSettingsRepository playerSettingsRepository;

    public NightVisionService(PlayerSettingsRepository playerSettingsRepository) {
        this.playerSettingsRepository = playerSettingsRepository;
    }

    public void turnOnNightVision(String initiator, Player player) {
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        playerSettings.setNightVision(initiator, true);
        playerSettingsRepository.save(playerSettings);
        if (playerSettings.isNightVisionEnabled()) {
            sendEvent(new NightVisionOnEvent(player));
        }
    }

    public void turnOffNightVision(String initiator, Player player) {
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        playerSettings.setNightVision(initiator, false);
        playerSettingsRepository.save(playerSettings);
        if (!playerSettings.isNightVisionEnabled()) {
            sendEvent(new NightVisionOffEvent(player));
        }
    }
}
