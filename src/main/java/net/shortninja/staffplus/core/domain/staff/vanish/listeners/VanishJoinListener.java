package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.core.domain.staff.vanish.gui.VanishPlayersBukkitService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@IocListener(conditionalOnProperty = "vanish-module.enabled=true")
public class VanishJoinListener implements Listener {

    private final VanishPlayersBukkitService vanishPlayersBukkitService;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final VanishServiceImpl vanishService;

    public VanishJoinListener(VanishPlayersBukkitService vanishPlayersBukkitService, PlayerSettingsRepository playerSettingsRepository, VanishServiceImpl vanishService) {
        this.vanishPlayersBukkitService = vanishPlayersBukkitService;
        this.playerSettingsRepository = playerSettingsRepository;
        this.vanishService = vanishService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hideJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        vanishPlayersBukkitService.updateVanish(player);
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        if (playerSettings.isVanished()) {
            vanishService.addVanish(player, playerSettings.getVanishType());
            event.setJoinMessage("");
        }
    }
}
