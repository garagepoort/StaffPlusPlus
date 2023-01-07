package net.shortninja.staffplus.core.domain.player.potioneffects;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@IocBean
public class PotionEffectService {

    private final PlayerSettingsRepository playerSettingsRepository;

    public PotionEffectService(PlayerSettingsRepository playerSettingsRepository) {
        this.playerSettingsRepository = playerSettingsRepository;
    }

    public void removeAllPotionEffects(Player player) {
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        playerSettings.isNightVisionEnabled();
        player.getActivePotionEffects().stream()
            .map(PotionEffect::getType)
            .filter(type -> !playerSettings.isNightVisionEnabled() || !type.equals(PotionEffectType.NIGHT_VISION))
            .forEach(player::removePotionEffect);
    }
}
