package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.vanish.gui.VanishPlayersBukkitService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@IocBean(conditionalOnProperty = "vanish-module.enabled=true")
@IocListener
public class VanishJoinListener implements Listener {

    private final VanishPlayersBukkitService vanishPlayersBukkitService;
    private final PlayerSettingsRepository playerSettingsRepository;

    public VanishJoinListener(VanishPlayersBukkitService vanishPlayersBukkitService, PlayerSettingsRepository playerSettingsRepository) {
        this.vanishPlayersBukkitService = vanishPlayersBukkitService;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hideJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        vanishPlayersBukkitService.updateVanish(player);
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        if (playerSettings.isVanished()) {
            vanishPlayersBukkitService.vanishPlayers(player, playerSettings.getVanishType());
            event.setJoinMessage("");
        }
    }
}
