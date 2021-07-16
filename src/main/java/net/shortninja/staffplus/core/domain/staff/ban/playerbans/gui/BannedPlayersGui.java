package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BannedPlayersGui extends PagedGui {

    private final BannedPlayerItemBuilder bannedPlayerItemBuilder = StaffPlus.get().getIocContainer().get(BannedPlayerItemBuilder.class);

    public BannedPlayersGui(Player player, String title, int page, Supplier<AbstractGui> backGuiSupplier) {
        super(player, title, page, backGuiSupplier);
    }

    @Override
    protected BannedPlayersGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new BannedPlayersGui(player, title, page, this.previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                int banId = Integer.parseInt(StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item));
                Ban ban = StaffPlus.get().getIocContainer().get(BanService.class).getById(banId);
                new ManageBannedPlayerGui("Player: " + ban.getTargetName(), ban, () -> new BannedPlayersGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier())).show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return StaffPlus.get().getIocContainer().get(BanService.class).getAllPaged(offset, amount).stream()
            .map(bannedPlayerItemBuilder::build)
            .collect(Collectors.toList());
    }
}