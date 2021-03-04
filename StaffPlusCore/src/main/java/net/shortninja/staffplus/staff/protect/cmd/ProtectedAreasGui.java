package net.shortninja.staffplus.staff.protect.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.protect.ProtectedArea;
import net.shortninja.staffplus.common.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ProtectedAreasGui extends PagedGui {

    public ProtectedAreasGui(Player player, String title, int page, Supplier<AbstractGui> previousGuiSupplier) {
        super(player, title, page, previousGuiSupplier);
    }

    @Override
    protected ProtectedAreasGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new ProtectedAreasGui(player, title, page, previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int protectedAreaId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    ProtectedArea protectedArea = IocContainer.getProtectService().getById(protectedAreaId);
                    new ManageProtectedAreaGui( "Protected area: " + protectedArea.getName(), protectedArea, () -> new ProtectedAreasGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier())).show(player);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.getProtectService().getAllProtectedAreasPaginated(offset, amount).stream()
            .map(ProtectedAreaItemBuilder::build)
            .collect(Collectors.toList());
    }
}