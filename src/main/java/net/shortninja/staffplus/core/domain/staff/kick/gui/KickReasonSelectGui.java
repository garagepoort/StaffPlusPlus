package net.shortninja.staffplus.core.domain.staff.kick.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.SimpleItemBuilder;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickReasonConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.getInventorySize;

public class KickReasonSelectGui extends AbstractGui {
    private final Player staff;
    private final SppPlayer targetPlayer;
    private List<KickReasonConfiguration> kickReasonConfigurations;

    public KickReasonSelectGui(Player staff, SppPlayer targetPlayer, List<KickReasonConfiguration> kickReasonConfigurations) {
        super(getInventorySize(StaffPlus.get().getIocContainer().get(Options.class).kickConfiguration.getKickReasons().size()), "Select the reason for kick");
        this.staff = staff;
        this.targetPlayer = targetPlayer;
        this.kickReasonConfigurations = kickReasonConfigurations;
    }

    public KickReasonSelectGui(Player staff, List<KickReasonConfiguration> kickReasonConfigurations) {
        super(getInventorySize(StaffPlus.get().getIocContainer().get(Options.class).kickConfiguration.getKickReasons().size()), "Select the reason for kick");
        this.staff = staff;
        this.targetPlayer = null;
        this.kickReasonConfigurations = kickReasonConfigurations;
    }

    @Override
    public void buildGui() {
        int count = 0;
        for (KickReasonConfiguration r : kickReasonConfigurations) {
            setItem(count, SimpleItemBuilder.build(r.getReason(), r.getLore(), r.getMaterial()), new SelectKickReasonAction(r, staff, targetPlayer));
            count++;
        }
    }
}