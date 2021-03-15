package net.shortninja.staffplus.staff.warn.warnings.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MyWarningsGui extends PagedGui {

    private final Permission permission = IocContainer.getPermissionHandler();
    private final WarnService warnService = IocContainer.getWarnService();

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
            public void click(Player player, ItemStack item, int slot) {
                if (options.appealConfiguration.isEnabled() && permission.has(player, options.appealConfiguration.getCreateAppealPermission())) {
                    int warningId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
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
        return IocContainer.getWarnService()
            .getWarnings(player.getUniqueId(), offset, amount, false)
            .stream()
            .map(WarningItemBuilder::build)
            .collect(Collectors.toList());
    }
}