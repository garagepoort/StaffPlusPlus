package net.shortninja.staffplus.domain.staff.mode.config.modeitems.vanish;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.session.PlayerSession;
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
