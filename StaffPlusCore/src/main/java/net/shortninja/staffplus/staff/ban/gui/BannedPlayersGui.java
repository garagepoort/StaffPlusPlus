package net.shortninja.staffplus.staff.ban.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.ban.Ban;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BannedPlayersGui extends PagedGui {

    public BannedPlayersGui(Player player, String title, int page, Supplier<AbstractGui> backGuiSupplier) {
        super(player, title, page, backGuiSupplier);
    }

    @Override
    protected void getNextUi(Player player, SppPlayer target, String title, int page) {
        new BannedPlayersGui(player, title, page, this.previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int banId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    Ban ban = IocContainer.getBanService().getById(banId);
                    new ManageBannedPlayerGui(player, "Player: " + ban.getPlayerName(), ban, () -> new BannedPlayersGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier()));
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
        return IocContainer.getBanService().getAllPaged(offset, amount).stream()
            .map(BannedPlayerItemBuilder::build)
            .collect(Collectors.toList());
    }
}