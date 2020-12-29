package net.shortninja.staffplus.staff.mute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mute.Mute;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageMutedPlayerGui extends AbstractGui {
    private static final int SIZE = 54;

    private final SessionManager sessionManager = IocContainer.getSessionManager();

    public ManageMutedPlayerGui(Player player, String title, Mute mute, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);

        IAction unmuteAction = new UnmutePlayerAction();

        setItem(13, MutedPlayerItemBuilder.build(mute), null);

        addUnmuteItem(mute, unmuteAction, 30);
        addUnmuteItem(mute, unmuteAction, 31);
        addUnmuteItem(mute, unmuteAction, 32);
        addUnmuteItem(mute, unmuteAction, 39);
        addUnmuteItem(mute, unmuteAction, 40);
        addUnmuteItem(mute, unmuteAction, 41);

        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private void addUnmuteItem(Mute mute, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createRedColoredGlass("Unmute player", "Click to unmute this player"))
                .setAmount(1)
                .build(), String.valueOf(mute.getId()));
        setItem(slot, item, action);
    }
}