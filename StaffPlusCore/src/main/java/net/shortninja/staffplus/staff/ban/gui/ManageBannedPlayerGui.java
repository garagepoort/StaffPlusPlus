package net.shortninja.staffplus.staff.ban.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.ban.Ban;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManageBannedPlayerGui extends AbstractGui {
    private static final int SIZE = 54;

    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final Options options = IocContainer.getOptions();
    private GuiItemConfig guiItemConfig;

    public ManageBannedPlayerGui(Player player, String title, Ban ban) {
        super(SIZE, title);
        guiItemConfig = options.banConfiguration.getGuiItemConfig();



        IAction unbanAction = new UnbanPlayerAction();

        IAction toOverviewAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                new BannedPlayersGui(player, guiItemConfig.getTitle(), 0);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        setItem(13, BannedPlayerItemBuilder.build(ban), null);

        addUnbanItem(ban, unbanAction, 30);
        addUnbanItem(ban, unbanAction, 31);
        addUnbanItem(ban, unbanAction, 32);
        addUnbanItem(ban, unbanAction, 39);
        addUnbanItem(ban, unbanAction, 40);
        addUnbanItem(ban, unbanAction, 41);

        addToOverviewItem(ban, toOverviewAction, 27);
        addToOverviewItem(ban, toOverviewAction, 28);
        addToOverviewItem(ban, toOverviewAction, 36);
        addToOverviewItem(ban, toOverviewAction, 37);

        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private void addToOverviewItem(Ban ban, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createGrayColoredGlass("Back", "Click to go back to the overview"))
                .setAmount(1)
                .build(), String.valueOf(ban.getId()));
        setItem(slot, item, action);
    }

    private void addUnbanItem(Ban ban, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createRedColoredGlass("Unban player", "Click to unban this player"))
                .setAmount(1)
                .build(), String.valueOf(ban.getId()));
        setItem(slot, item, action);
    }
}