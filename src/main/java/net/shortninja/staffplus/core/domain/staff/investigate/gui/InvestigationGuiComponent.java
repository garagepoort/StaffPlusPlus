package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedSelector;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationEvidenceService;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationItemBuilder;
import net.shortninja.staffplusplus.investigate.evidence.Evidence;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

@IocBean
public class InvestigationGuiComponent {

    private final InvestigationService investigationService;
    private final InvestigationEvidenceService investigationEvidenceService;
    private final InvestigationItemBuilder investigationItemBuilder;
    private final IProtocolService protocolService;
    private final Options options;
    private final PermissionHandler permissionHandler;

    public InvestigationGuiComponent(InvestigationService investigationService, InvestigationEvidenceService investigationEvidenceService, InvestigationItemBuilder investigationItemBuilder, IProtocolService protocolService, Options options, PermissionHandler permissionHandler) {
        this.investigationService = investigationService;
        this.investigationEvidenceService = investigationEvidenceService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.protocolService = protocolService;
        this.options = options;
        this.permissionHandler = permissionHandler;
    }

    public void addEvidenceButton(AbstractGui abstractGui, int slot, Evidence evidence) {
        if (!options.investigationConfiguration.isEnabled()) {
            return;
        }
        ItemStack book = Items.createAnvil("Add this as evidence to investigation", "Click to link evidence to investigation");
        abstractGui.setItem(slot, book, new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                permissionHandler.validate(player, options.investigationConfiguration.getLinkEvidencePermission());

                new PagedSelector(player, "Select investigation to link", 0,
                    () -> abstractGui,
                    selected -> {
                        int investigationId = Integer.parseInt(protocolService.getVersionProtocol().getNbtString(selected));
                        Investigation investigation = investigationService.getInvestigation(investigationId);
                        investigationEvidenceService.linkEvidence(player, investigation, evidence);
                    },
                    (player1, target, offset, amount) -> investigationService.getAllInvestigations(offset, amount).stream()
                        .map(investigationItemBuilder::build)
                        .collect(Collectors.toList()))
                    .show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        });
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
