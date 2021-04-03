package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MyWarningsGui extends PagedGui {

    private final PermissionHandler permission = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
    private final WarnService warnService = StaffPlus.get().getIocContainer().get(WarnService.class);
    private final WarningItemBuilder warningItemBuilder = StaffPlus.get().getIocContainer().get(WarningItemBuilder.class);

    public MyWarningsGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected MyWarningsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new MyWarningsGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                if (options.appealConfiguration.isEnabled() && permission.has(player, options.appealConfiguration.getCreateAppealPermission())) {
                    int warningId = Integer.parseInt(StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item));
                    Warning warning = warnService.getWarning(warningId);
                    new ManageWarningGui(player, "Warning", warning, () -> new MyWarningsGui(player, getTitle(), getCurrentPage())).show(player);
                }
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return StaffPlus.get().getIocContainer().get(WarnService.class)
            .getWarnings(player.getUniqueId(), offset, amount, false)
            .stream()
            .map(warningItemBuilder::build)
            .collect(Collectors.toList());
    }
}