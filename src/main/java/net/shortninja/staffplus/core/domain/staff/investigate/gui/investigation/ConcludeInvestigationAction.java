package net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation;

import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ConcludeInvestigationAction implements IAction {

    private final InvestigationService investigationService;
    private final Investigation investigation;

    public ConcludeInvestigationAction(InvestigationService investigationService, Investigation investigation) {
        this.investigationService = investigationService;
        this.investigation = investigation;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        investigationService.concludeInvestigation(player, investigation.getId());
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
