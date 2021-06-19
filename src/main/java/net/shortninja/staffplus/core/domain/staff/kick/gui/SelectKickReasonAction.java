package net.shortninja.staffplus.core.domain.staff.kick.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.kick.KickService;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickReasonConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SelectKickReasonAction implements IAction {
    private final KickReasonConfiguration kickReasonConfiguration;
    private final Player staff;
    private final SppPlayer targetPlayer;
    private final KickService kickService = StaffPlus.get().getIocContainer().get(KickService.class);

    public SelectKickReasonAction(KickReasonConfiguration kickReasonConfiguration, Player staff, SppPlayer targetPlayer) {
        this.kickReasonConfiguration = kickReasonConfiguration;
        this.staff = staff;
        this.targetPlayer = targetPlayer;
    }


    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        kickService.kick(staff, targetPlayer, kickReasonConfiguration.getReason());
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
