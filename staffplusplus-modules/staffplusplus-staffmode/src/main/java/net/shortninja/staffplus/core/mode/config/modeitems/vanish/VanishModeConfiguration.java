package net.shortninja.staffplus.core.mode.config.modeitems.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.mode.config.ModeItemConfigTransformer;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.inventory.ItemStack;

@IocBean
@ConfigProperties("staffmode-modules:modules.vanish-module")
public class VanishModeConfiguration extends ModeItemConfiguration {

    @ConfigProperty("item-off")
    @ConfigTransformer(ModeItemConfigTransformer.class)
    private ItemStack modeVanishItemOff;

    public ItemStack getModeVanishItem(PlayerSettings session, VanishType vanishType) {
        return session.getVanishType() == vanishType ? item : modeVanishItemOff;
    }

    @Override
    public String getIdentifier() {
        return "vanish-module";
    }
}
