package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishStrategy;
import net.shortninja.staffplusplus.vanish.VanishStrategyProvider;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Collections;
import java.util.List;

@IocBukkitListener
public class VanishPlayersBukkitService implements Listener {

    private final VanishConfiguration vanishConfiguration;
    private final PlayerManager playerManager;

    public VanishPlayersBukkitService(VanishConfiguration vanishConfiguration, PlayerManager playerManager) {
        this.vanishConfiguration = vanishConfiguration;
        this.playerManager = playerManager;
    }

    private List<VanishStrategy> getVanishStrategies() {
        RegisteredServiceProvider<VanishStrategyProvider> provider = Bukkit.getServicesManager().getRegistration(VanishStrategyProvider.class);
        if (provider != null) {
            VanishStrategyProvider vanishStrategyProvider = provider.getProvider();
            return vanishStrategyProvider.getStrategies();
        }
        return Collections.emptyList();
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
                getVanishStrategies().stream()
                    .filter(s -> s.getVanishType() == event.getType())
                    .forEach(v -> v.unvanish(sppPlayer, vanishConfiguration));
            });
    }

    private void vanishPlayers(Player player, VanishType vanishType) {
        playerManager.getOnlinePlayer(player.getUniqueId())
            .ifPresent(sppPlayer -> {
                getVanishStrategies().stream()
                    .filter(v -> v.getVanishType() == vanishType)
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("&CNo Suitable vanish strategy found for type [" + vanishType + "]"))
                    .vanish(sppPlayer, vanishConfiguration);
            });
    }

    public void updateVanish(Player player) {
        playerManager.getOnlinePlayer(player.getUniqueId())
            .ifPresent(sppPlayer -> {
                if (!vanishConfiguration.enabled) {
                    return;
                }
                getVanishStrategies().forEach(v -> v.updateVanish(sppPlayer, vanishConfiguration));
            });
    }
}
