package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class NoneVanishStrategy implements VanishStrategy {


    @Override
    public void vanish(SppPlayer player) {
    }

    @Override
    public void updateVanish(SppPlayer player) {
    }

    @Override
    public void unvanish(SppPlayer player) {
    }

    @Override
    public VanishType getVanishType() {
        return VanishType.NONE;
    }

}
