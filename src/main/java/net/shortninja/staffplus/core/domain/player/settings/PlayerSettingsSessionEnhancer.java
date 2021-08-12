package net.shortninja.staffplus.core.domain.player.settings;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class PlayerSettingsSessionEnhancer implements SessionEnhancer {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final ModeProvider modeProvider;

    public PlayerSettingsSessionEnhancer(PlayerSettingsRepository playerSettingsRepository, ModeProvider modeProvider) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.modeProvider = modeProvider;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        Player player = Bukkit.getPlayer(playerSession.getUuid());
        if (player != null) {
            PlayerSettings playerSettings = playerSettingsRepository.get(player);
            playerSession.setInStaffMode(playerSettings.isInStaffMode());
            playerSession.setVanishType(playerSettings.getVanishType());
            if (playerSettings.isInStaffMode()) {
                Optional<GeneralModeConfiguration> configuration = modeProvider.getConfiguration(playerSettings.getModeName().orElse(null));
                configuration.ifPresent(playerSession::setModeConfig);
            }
        }
    }
}
