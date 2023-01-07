package net.shortninja.staffplus.core.application.session.enhancers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class PlayerSettingsSessionEnhancer implements SessionEnhancer {

    private final PlayerSettingsRepository playerSettingsRepository;

    public PlayerSettingsSessionEnhancer(PlayerSettingsRepository playerSettingsRepository) {
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        Player player = Bukkit.getPlayer(playerSession.getUuid());
        if (player != null) {
            PlayerSettings playerSettings = playerSettingsRepository.get(player);
            playerSession.setVanishType(playerSettings.getVanishType());
        }
    }
}
