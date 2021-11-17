package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplusplus.investigate.evidence.Evidence;
import org.bukkit.inventory.ItemStack;

@IocBean
public class InvestigationGuiComponent {

    private final Options options;

    public InvestigationGuiComponent(Options options) {
        this.options = options;
    }

    public void addEvidenceButton(TubingGui.Builder builder, int slot, Evidence evidence, String backAction) {
        if (!options.investigationConfiguration.isEnabled()) {
            return;
        }

        ItemStack book = Items.createAnvil("Add this as evidence to investigation", "Click to link evidence to investigation");
        String action = GuiActionBuilder.builder()
            .action("manage-investigation-evidence/view/investigation-link")
            .param("backAction", backAction)
            .param("evidenceId", String.valueOf(evidence.getId()))
            .param("evidenceType", evidence.getEvidenceType())
            .param("evidenceDescription", evidence.getDescription())
            .build();

        builder.addItem(action, slot, book);
    }
}
