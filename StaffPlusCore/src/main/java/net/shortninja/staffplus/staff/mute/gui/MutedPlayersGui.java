package net.shortninja.staffplus.staff.mute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.mute.Mute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MutedPlayersGui extends PagedGui {

    public MutedPlayersGui(Player player, String title, int page, Supplier<AbstractGui> backGuiSupplier) {
        super(player, title, page, backGuiSupplier);
    }

    @Override
    protected MutedPlayersGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new MutedPlayersGui(player, title, page, this.previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                int muteId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                Mute mute = IocContainer.getMuteService().getById(muteId);
                new ManageMutedPlayerGui("Player: " + mute.getTargetName(), mute, () -> new MutedPlayersGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier())).show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.getMuteService().getAllPaged(offset, amount).stream()
            .map(MutedPlayerItemBuilder::build)
            .collect(Collectors.toList());
    }
}