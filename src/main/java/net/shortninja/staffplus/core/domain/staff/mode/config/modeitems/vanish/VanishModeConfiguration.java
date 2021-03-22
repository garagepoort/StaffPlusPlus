package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.inventory.ItemStack;

public class VanishModeConfiguration extends ModeItemConfiguration {

    private ItemStack modeVanishItemOff;

    public VanishModeConfiguration(String identifier, ItemStack modeVanishItemOff) {
        super(identifier);
        this.modeVanishItemOff = StaffPlus.get().versionProtocol.addNbtString(modeVanishItemOff, identifier);;
    }

    public ItemStack getModeVanishItem(PlayerSession session, VanishType vanishType) {
        return session.getVanishType() == vanishType ? getItem() : modeVanishItemOff;
    }
}
