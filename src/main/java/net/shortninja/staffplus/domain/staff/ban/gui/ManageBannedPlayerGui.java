package net.shortninja.staffplus.domain.staff.ban.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.domain.staff.ban.Ban;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.Items;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageBannedPlayerGui extends AbstractGui {
    private static final int SIZE = 54;

    private final Ban ban;

    public ManageBannedPlayerGui(String title, Ban ban, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.ban = ban;
    }

    @Override
    public void buildGui() {
        IAction unbanAction = new UnbanPlayerAction();

        setItem(13, BannedPlayerItemBuilder.build(ban), null);

        addUnbanItem(ban, unbanAction, 30);
        addUnbanItem(ban, unbanAction, 31);
        addUnbanItem(ban, unbanAction, 32);
        addUnbanItem(ban, unbanAction, 39);
        addUnbanItem(ban, unbanAction, 40);
        addUnbanItem(ban, unbanAction, 41);
    }

    private void addUnbanItem(Ban ban, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createRedColoredGlass("Unban player", "Click to unban this player"))
                .setAmount(1)
                .build(), String.valueOf(ban.getId()));
        setItem(slot, item, action);
    }

}