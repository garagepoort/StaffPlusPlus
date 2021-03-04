package net.shortninja.staffplus.staff.warn.warnings.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.common.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ManageAppealedWarningsGui extends PagedGui {

    private final Options options = IocContainer.getOptions();
    private final WarnService warnService = IocContainer.getWarnService();

    public ManageAppealedWarningsGui(Player player, SppPlayer target, String title, int currentPage) {
        super(player, target, title, currentPage);
    }

    @Override
    protected AbstractGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new ManageAppealedWarningsGui(player, target, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                int warningId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                Warning warning = warnService.getWarning(warningId);
                new ManageWarningGui(player, "Manage warning", warning, () -> new ManageAppealedWarningsGui(player, getTarget(), getTitle(), getCurrentPage())).show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return warnService.getAppealedWarnings(offset, amount)
            .stream().map(WarningItemBuilder::build)
            .collect(Collectors.toList());
    }
}