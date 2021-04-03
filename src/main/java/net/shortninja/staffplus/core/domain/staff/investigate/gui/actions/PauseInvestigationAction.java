package net.shortninja.staffplus.core.domain.staff.investigate.gui.actions;

import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PauseInvestigationAction implements IAction {

    private final InvestigationService investigationService;

    public PauseInvestigationAction(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        investigationService.pauseInvestigation(player);
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
