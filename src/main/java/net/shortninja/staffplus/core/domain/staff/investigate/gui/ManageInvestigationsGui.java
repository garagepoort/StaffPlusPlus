package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManageInvestigationsGui extends PagedGui {

    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;
    private final IProtocolService protocolService;
    private final Supplier<AbstractGui> goback;

    public ManageInvestigationsGui(Player player, SppPlayer target, String title, int currentPage, InvestigationService investigationService, InvestigationItemBuilder investigationItemBuilder, IProtocolService protocolService) {
        super(player, target, title, currentPage);
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.protocolService = protocolService;
        goback = () -> new ManageInvestigationsGui(player, getTarget(), getTitle(), getCurrentPage(),
            investigationService,
            investigationItemBuilder,
            protocolService);
    }

    @Override
    protected AbstractGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new ManageInvestigationsGui(player, target, title, page, investigationService, investigationItemBuilder, protocolService);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {

                int investigationId = Integer.parseInt(protocolService.getVersionProtocol().getNbtString(item));
                Investigation investigation = investigationService.getInvestigation(investigationId);
                new ManageInvestigationGui(player,
                    "Manage Investigation",
                    investigation,
                    goback,
                    investigationService,
                    investigationItemBuilder)
                    .show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        if (target == null) {
            return investigationService.getAllInvestigations(offset, amount)
                .stream().map(investigationItemBuilder::build)
                .collect(Collectors.toList());
        }
        return investigationService.getInvestigationsForInvestigated(target, offset, amount)
            .stream().map(investigationItemBuilder::build)
            .collect(Collectors.toList());
    }
}