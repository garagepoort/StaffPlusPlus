package net.shortninja.staffplus.core.mode.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionEnhancer;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.mode.ModeProvider;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean
@IocMultiProvider(SessionEnhancer.class)
public class StaffModeSessionEnhancer implements SessionEnhancer {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final ModeProvider modeProvider;

    public StaffModeSessionEnhancer(PlayerSettingsRepository playerSettingsRepository, ModeProvider modeProvider) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.modeProvider = modeProvider;
    }

    @Override
    public void enhance(PlayerSession playerSession) {
        Player player = Bukkit.getPlayer(playerSession.getUuid());
        if (player != null) {
            PlayerSettings playerSettings = playerSettingsRepository.get(player);
            playerSession.setInStaffMode(playerSettings.isInStaffMode());
            if (playerSettings.isInStaffMode()) {
                Optional<GeneralModeConfiguration> configuration = modeProvider.getConfiguration(playerSettings.getModeName().orElse(null));
                configuration.ifPresent(c -> playerSession.set("modeConfig", c));
            }
        }
    }
}
