package net.shortninja.staffplus.staff.ban.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.ban.Ban;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class BannedPlayersGui extends PagedGui {

    public BannedPlayersGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected void getNextUi(Player player, String title, int page) {
        new BannedPlayersGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int banId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    Ban ban = IocContainer.getBanService().getById(banId);
                    new ManageBannedPlayerGui(player, "Player: " + ban.getPlayerName(), ban);
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
        return IocContainer.getBanService().getAllPaged(offset, amount).stream()
            .map(BannedPlayerItemBuilder::build)
            .collect(Collectors.toList());
    }
}