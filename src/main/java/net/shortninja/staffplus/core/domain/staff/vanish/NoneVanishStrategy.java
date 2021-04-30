package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class NoneVanishStrategy implements VanishStrategy {


    @Override
    public void vanish(Player player) {
    }

    @Override
    public void updateVanish(Player player) {
    }

    @Override
    public void unvanish(Player player) {
    }

    @Override
    public VanishType getVanishType() {
        return VanishType.NONE;
    }

}
