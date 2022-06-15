package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishStrategy;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@IocListener
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
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(event.getPlayer().getUniqueId())
            .orElseThrow(() -> new PlayerNotFoundException(event.getPlayer().getName()));
        vanishStrategies.stream()
            .filter(s -> s.getVanishType() == event.getType())
            .forEach(v -> v.unvanish(sppPlayer));
    }

    private void vanishPlayers(Player player, VanishType vanishType) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow(() -> new PlayerNotFoundException(player.getName()));
        vanishStrategies.stream()
            .filter(v -> v.getVanishType() == vanishType)
            .findFirst()
            .orElseThrow(() -> new BusinessException("&CNo Suitable vanish strategy found for type [" + vanishType + "]"))
            .vanish(sppPlayer);
    }

    public void updateVanish(Player player) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow(() -> new PlayerNotFoundException(player.getName()));
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        vanishStrategies.forEach(v -> v.updateVanish(sppPlayer));
    }

}
