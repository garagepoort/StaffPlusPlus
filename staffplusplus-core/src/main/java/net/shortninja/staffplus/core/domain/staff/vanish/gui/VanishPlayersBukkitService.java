package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishStrategy;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@IocBukkitListener
public class VanishPlayersBukkitService implements Listener {

    private final List<VanishStrategy> vanishStrategies;
    private final VanishConfiguration vanishConfiguration;
    private final PlayerManager playerManager;

    public VanishPlayersBukkitService(@IocMulti(VanishStrategy.class) List<VanishStrategy> vanishStrategies, VanishConfiguration vanishConfiguration, PlayerManager playerManager) {
        this.vanishStrategies = vanishStrategies;
        this.vanishConfiguration = vanishConfiguration;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onVanish(VanishOnEvent event) {
        VanishType vanishType = event.getType();
        vanishPlayers(event.getPlayer(), vanishType);
    }

    @EventHandler
    public void onUnvanish(VanishOffEvent event) {
        playerManager.getOnlinePlayer(event.getPlayer().getUniqueId())
            .ifPresent(sppPlayer -> {
                vanishStrategies.stream()
                    .filter(s -> s.getVanishType() == event.getType())
                    .forEach(v -> v.unvanish(sppPlayer));
            });
    }

    private void vanishPlayers(Player player, VanishType vanishType) {
        playerManager.getOnlinePlayer(player.getUniqueId())
            .ifPresent(sppPlayer -> {
                vanishStrategies.stream()
                    .filter(v -> v.getVanishType() == vanishType)
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("&CNo Suitable vanish strategy found for type [" + vanishType + "]"))
                    .vanish(sppPlayer);
            });
    }

    public void updateVanish(Player player) {
        playerManager.getOnlinePlayer(player.getUniqueId())
            .ifPresent(sppPlayer -> {
                if (!vanishConfiguration.enabled) {
                    return;
                }
                vanishStrategies.forEach(v -> v.updateVanish(sppPlayer));
            });
    }
}
