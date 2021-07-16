package net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManageInvestigationsGui extends PagedGui {

    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;
    private final Supplier<AbstractGui> goback;

    public ManageInvestigationsGui(Player player,
                                   String title,
                                   int currentPage,
                                   SppPlayer target,
                                   InvestigationService investigationService,
                                   InvestigationItemBuilder investigationItemBuilder,
                                   PlayerManager playerManager,
                                   IProtocolService protocolService) {
        super(player, target, title, currentPage);
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        goback = () -> new ManageInvestigationsGui(player, getTitle(), getCurrentPage(),
            target, investigationService,
            investigationItemBuilder,
            playerManager, protocolService);
    }

    public ManageInvestigationsGui(Player player,
                                   String title,
                                   int currentPage,
                                   SppPlayer target,
                                   Supplier<AbstractGui> previousGui,
                                   InvestigationService investigationService,
                                   InvestigationItemBuilder investigationItemBuilder,
                                   PlayerManager playerManager,
                                   IProtocolService protocolService) {
        super(player, target, title, currentPage, previousGui);
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        goback = () -> new ManageInvestigationsGui(player, getTitle(), getCurrentPage(),
            target, previousGui, investigationService,
            investigationItemBuilder,
            playerManager, protocolService);
    }

    @Override
    protected AbstractGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new ManageInvestigationsGui(player, title, page, target, getPreviousGuiSupplier(), investigationService, investigationItemBuilder, playerManager, protocolService);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                int investigationId = Integer.parseInt(protocolService.getVersionProtocol().getNbtString(item));
                Investigation investigation = investigationService.getInvestigation(investigationId);
                new ManageInvestigationGui(player,
                    "Manage Investigation",
                    investigation,
                    goback,
                    playerManager,
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