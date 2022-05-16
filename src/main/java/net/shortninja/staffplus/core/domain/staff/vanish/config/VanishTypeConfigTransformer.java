package net.shortninja.staffplus.core.domain.staff.vanish.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplusplus.vanish.VanishType;

public class VanishTypeConfigTransformer implements IConfigTransformer<VanishType, String> {
    @Override
    public VanishType mapConfig(String s) {
        return VanishType.valueOf(s);
    }
}
