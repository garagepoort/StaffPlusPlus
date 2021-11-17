package net.shortninja.staffplus.core.domain.staff.investigate.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.model.TubingGuiActions;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import org.bukkit.inventory.ItemStack;

@IocBean
public class InvestigationDetailViewBuilder {
    private static final int SIZE = 54;

    private final InvestigationItemBuilder investigationItemBuilder;

    public InvestigationDetailViewBuilder(InvestigationItemBuilder investigationItemBuilder) {
        this.investigationItemBuilder = investigationItemBuilder;
    }

    public TubingGui buildGui(Investigation investigation, String currentAction, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Manage investigation", SIZE);

        builder.addItem(getAction("manage-investigation-notes/view", investigation, currentAction), 11, Items.createPaper("Notes", "Go to notes overview"));
        builder.addItem(getAction("manage-investigation-evidence/view", investigation, currentAction), 15, Items.createAnvil("Evidence", "Go to evidence overview"));

        builder.addItem(TubingGuiActions.NOOP, 13, investigationItemBuilder.build(investigation));

        if (investigation.getStatus() == InvestigationStatus.OPEN) {
            ItemStack pauseItem = Items.createWhiteColoredGlass("Pause", "Click to take a break investigating");
            String pauseAction = "manage-investigations/pause";
            builder.addItem(pauseAction, 27, pauseItem);
            builder.addItem(pauseAction, 28, pauseItem);
            builder.addItem(pauseAction, 36, pauseItem);
            builder.addItem(pauseAction, 37, pauseItem);
        }

        if (investigation.getStatus() == InvestigationStatus.PAUSED) {
            ItemStack resumeItem = Items.createGreenColoredGlass("Resume", "Click to resume this investigation.");
            String resumeAction = "manage-investigations/resume?investigationId=" + investigation.getId();
            builder.addItem(resumeAction, 27, resumeItem);
            builder.addItem(resumeAction, 28, resumeItem);
            builder.addItem(resumeAction, 36, resumeItem);
            builder.addItem(resumeAction, 37, resumeItem);
        }

        if (investigation.getStatus() != InvestigationStatus.CONCLUDED) {
            ItemStack concludeItem = Items.createRedColoredGlass("Conclude", "Click to conclude this investigation");
            String concludeAction = "manage-investigations/conclude?investigationId=" + investigation.getId();
            builder.addItem(concludeAction, 34, concludeItem);
            builder.addItem(concludeAction, 35, concludeItem);
            builder.addItem(concludeAction, 43, concludeItem);
            builder.addItem(concludeAction, 44, concludeItem);
        }

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        return builder.build();
    }

    private String getAction(String action, Investigation investigation, String backAction) {
        return GuiActionBuilder.builder()
            .action(action)
            .param("investigationId", String.valueOf(investigation.getId()))
            .param("backAction", backAction)
            .build();
    }
}