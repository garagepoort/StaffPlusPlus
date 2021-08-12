package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

@IocBean
@IocListener
public class MobsAttackingVanishedListener implements Listener {
    private final OnlineSessionsManager sessionManager;
    private final PlayerSettingsRepository playerSettingsRepository;

    public MobsAttackingVanishedListener(OnlineSessionsManager sessionManager, PlayerSettingsRepository playerSettingsRepository) {
        this.sessionManager = sessionManager;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player && sessionManager.has(event.getTarget().getUniqueId())) {
            PlayerSettings playerSettings = playerSettingsRepository.get((OfflinePlayer) event.getTarget());
            if (playerSettings.getVanishType() == VanishType.TOTAL || playerSettings.getVanishType() == VanishType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }
}