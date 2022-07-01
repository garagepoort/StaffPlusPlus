package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.inventory.ItemStack;

public class VanishModeConfiguration extends ModeItemConfiguration {

    private final ItemStack modeVanishItemOff;

    public VanishModeConfiguration(IProtocolService protocolService, String identifier, ItemStack modeVanishItemOff) {
        super(identifier);
        this.modeVanishItemOff = protocolService.getVersionProtocol().addNbtString(modeVanishItemOff, identifier);
    }

    public ItemStack getModeVanishItem(PlayerSettings session, VanishType vanishType) {
        return session.getVanishType() == vanishType ? getItem() : modeVanishItemOff;
    }
}
