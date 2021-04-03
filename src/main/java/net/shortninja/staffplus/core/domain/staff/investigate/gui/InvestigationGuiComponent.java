package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedSelector;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.Evidence;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationEvidenceService;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation.InvestigationItemBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation.ManageInvestigationsGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

@IocBean
public class InvestigationGuiComponent {

    private final InvestigationService investigationService;
    private final InvestigationEvidenceService investigationEvidenceService;
    private final InvestigationItemBuilder investigationItemBuilder;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;
    private final Options options;
    private final PermissionHandler permissionHandler;

    public InvestigationGuiComponent(InvestigationService investigationService, InvestigationEvidenceService investigationEvidenceService, InvestigationItemBuilder investigationItemBuilder, PlayerManager playerManager, IProtocolService protocolService, Options options, PermissionHandler permissionHandler) {
        this.investigationService = investigationService;
        this.investigationEvidenceService = investigationEvidenceService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        this.options = options;
        this.permissionHandler = permissionHandler;
    }

    public void addEvidenceButton(AbstractGui abstractGui, int slot, Evidence evidence) {
        if(!options.investigationConfiguration.isEnabled()) {
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

    public void openManageInvestigationsGui(Player player, SppPlayer target) {
        new ManageInvestigationsGui(player, target, "Manage Investigation", 0, investigationService, investigationItemBuilder, playerManager, protocolService).show(player);
    }
}
