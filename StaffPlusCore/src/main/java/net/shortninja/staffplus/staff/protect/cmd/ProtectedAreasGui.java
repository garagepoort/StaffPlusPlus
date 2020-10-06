package net.shortninja.staffplus.staff.protect.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.protect.ProtectedArea;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ProtectedAreasGui extends PagedGui {

    public ProtectedAreasGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected void getNextUi(Player player, String title, int page) {
        new ProtectedAreasGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int protectedAreaId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    ProtectedArea protectedArea = IocContainer.getProtectService().getById(protectedAreaId);
                    new ManageProtectedAreaGui(player, "Protected area: " + protectedArea.getName(), protectedArea);
                });
            }

            @Override
            public boolean shouldClose() {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, int offset, int amount) {
        return IocContainer.getProtectService().getAllProtectedAreasPaginated(offset, amount).stream()
            .map(ProtectedAreaItemBuilder::build)
            .collect(Collectors.toList());
    }
}