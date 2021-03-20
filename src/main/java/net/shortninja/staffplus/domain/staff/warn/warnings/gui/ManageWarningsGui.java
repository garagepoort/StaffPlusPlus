package net.shortninja.staffplus.domain.staff.warn.warnings.gui;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.gui.PagedGui;
import net.shortninja.staffplus.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ManageWarningsGui extends PagedGui {

    private final WarnService warnService = IocContainer.getWarnService();

    public ManageWarningsGui(Player player, SppPlayer target, String title, int currentPage) {
        super(player, target, title, currentPage);
    }

    @Override
    protected AbstractGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new ManageWarningsGui(player, target, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {

                int warningId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                Warning warning = warnService.getWarning(warningId);
                new ManageWarningGui(player, "Manage warnings", warning, () -> new ManageWarningsGui(player, getTarget(), getTitle(), getCurrentPage())).show(player);
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
            return warnService.getAllWarnings(offset, amount, true)
                .stream().map(WarningItemBuilder::build)
                .collect(Collectors.toList());
        }
        return warnService.getWarnings(target.getId(), offset, amount, true)
            .stream().map(WarningItemBuilder::build)
            .collect(Collectors.toList());
    }
}