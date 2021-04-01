package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.actions.PauseInvestigationAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageInvestigationGui extends AbstractGui {
    private static final int SIZE = 54;


    private final Player player;
    private final Investigation investigation;
    private final Supplier<AbstractGui> goBack;

    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;

    public ManageInvestigationGui(Player player, String title, Investigation investigation, Supplier<AbstractGui> previousGuiSupplier, InvestigationService investigationService, InvestigationItemBuilder investigationItemBuilder) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.investigation = investigation;
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
        goBack = () -> new ManageInvestigationGui(player, title, investigationService.getInvestigation(investigation.getId()), previousGuiSupplier, investigationService, investigationItemBuilder);
    }

    @Override
    public void buildGui() {
        setItem(13, investigationItemBuilder.build(investigation), null);

        PauseInvestigationAction pauseAction = new PauseInvestigationAction(investigationService);

        addPauseItem(pauseAction, 27);
        addPauseItem(pauseAction, 28);
        addPauseItem(pauseAction, 36);
        addPauseItem(pauseAction, 37);
    }

    private void addPauseItem(IAction action, int slot) {
        ItemStack itemStack = Items.createWhiteColoredGlass("Pause", "Click to take a break investigating");
        itemStack.setAmount(1);
        setItem(slot, itemStack, action);
    }
}