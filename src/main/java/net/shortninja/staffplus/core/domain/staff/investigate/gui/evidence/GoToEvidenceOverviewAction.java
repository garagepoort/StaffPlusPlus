package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class GoToEvidenceOverviewAction implements IAction {
    private final Investigation investigation;
    private final Supplier<AbstractGui> goBack;

    public GoToEvidenceOverviewAction(Investigation investigation, Supplier<AbstractGui> goBack) {
        this.investigation = investigation;
        this.goBack = goBack;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        new EvidenceOverviewGui(player, "Evidence Overview", 0, investigation, goBack).show(player);
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
