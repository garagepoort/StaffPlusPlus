package net.shortninja.staffplus.staff.warn.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MyWarningsGui extends PagedGui {

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
                // Do nothing for now
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
                .getWarnings(player.getUniqueId(), offset, amount)
                .stream()
                .map(WarningItemBuilder::build)
                .collect(Collectors.toList());
    }
}