package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishStrategy;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@IocBean
@IocListener
public class VanishPlayersBukkitService implements Listener {

    private final List<VanishStrategy> vanishStrategies;
    private final VanishConfiguration vanishConfiguration;

    public VanishPlayersBukkitService(@IocMulti(VanishStrategy.class) List<VanishStrategy> vanishStrategies, VanishConfiguration vanishConfiguration) {
        this.vanishStrategies = vanishStrategies;
        this.vanishConfiguration = vanishConfiguration;
    }

    @EventHandler
    public void onVanish(VanishOnEvent event) {
        VanishType vanishType = event.getType();
        vanishPlayers(event.getPlayer(), vanishType);
    }

    @EventHandler
    public void onUnvanish(VanishOffEvent event) {
        vanishStrategies.stream()
            .filter(s -> s.getVanishType() == event.getType())
            .forEach(v -> v.unvanish(event.getPlayer()));
    }

    public void vanishPlayers(Player player, VanishType vanishType) {
        vanishStrategies.stream()
            .filter(v -> v.getVanishType() == vanishType)
            .findFirst()
            .orElseThrow(() -> new BusinessException("&CNo Suitable vanish strategy found for type [" + vanishType + "]")).vanish(player);
    }

    public void updateVanish(Player player) {
        if (!vanishConfiguration.vanishEnabled) {
            return;
        }
        vanishStrategies.forEach(v -> v.updateVanish(player));
    }

}
