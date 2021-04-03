package net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation;

import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence.GoToEvidenceOverviewAction;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.notes.GoToNoteOverviewAction;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageInvestigationGui extends AbstractGui {
    private static final int SIZE = 54;


    private final Investigation investigation;
    private final Supplier<AbstractGui> goBack;

    private final PlayerManager playerManager;
    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;

    public ManageInvestigationGui(Player player,
                                  String title,
                                  Investigation investigation,
                                  Supplier<AbstractGui> previousGuiSupplier,
                                  PlayerManager playerManager,
                                  InvestigationService investigationService,
                                  InvestigationItemBuilder investigationItemBuilder) {
        super(SIZE, title, previousGuiSupplier);
        this.investigation = investigation;
        this.playerManager = playerManager;
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
        goBack = () -> new ManageInvestigationGui(player, title, investigationService.getInvestigation(investigation.getId()), previousGuiSupplier, playerManager, investigationService, investigationItemBuilder);
    }

    @Override
    public void buildGui() {
        setItem(11, Items.createPaper("Notes", "Go to notes overview"), new GoToNoteOverviewAction(investigation, goBack));
        setItem(13, investigationItemBuilder.build(investigation), null);
        setItem(15, Items.createAnvil("Evidence", "Go to evidence overview"), new GoToEvidenceOverviewAction(investigation, goBack));

        if (investigation.getStatus() == InvestigationStatus.OPEN) {
            PauseInvestigationAction pauseAction = new PauseInvestigationAction(investigationService);
            addPauseItem(pauseAction, 27);
            addPauseItem(pauseAction, 28);
            addPauseItem(pauseAction, 36);
            addPauseItem(pauseAction, 37);
        }
        if (investigation.getStatus() == InvestigationStatus.PAUSED) {
            ResumeInvestigationAction pauseAction = new ResumeInvestigationAction(investigationService, investigation, playerManager);
            addResumeItem(pauseAction, 27);
            addResumeItem(pauseAction, 28);
            addResumeItem(pauseAction, 36);
            addResumeItem(pauseAction, 37);
        }

        if (investigation.getStatus() != InvestigationStatus.CONCLUDED) {
            ConcludeInvestigationAction pauseAction = new ConcludeInvestigationAction(investigationService, investigation);
            addConcludeItem(pauseAction, 34);
            addConcludeItem(pauseAction, 35);
            addConcludeItem(pauseAction, 43);
            addConcludeItem(pauseAction, 44);
        }
    }

    private void addResumeItem(IAction action, int slot) {
        ItemStack itemStack = Items.createGreenColoredGlass("Resume", "Click to resume this investigation.");
        itemStack.setAmount(1);
        setItem(slot, itemStack, action);
    }

    private void addPauseItem(IAction action, int slot) {
        ItemStack itemStack = Items.createWhiteColoredGlass("Pause", "Click to take a break investigating");
        itemStack.setAmount(1);
        setItem(slot, itemStack, action);
    }

    private void addConcludeItem(IAction action, int slot) {
        ItemStack itemStack = Items.createRedColoredGlass("Conclude", "Click to conclude this investigation");
        itemStack.setAmount(1);
        setItem(slot, itemStack, action);
    }
}