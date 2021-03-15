package net.shortninja.staffplus.common.gui;

import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.common.confirmation.CancelAction;
import net.shortninja.staffplus.common.confirmation.ConfirmationAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfirmationGui extends AbstractGui {
    private final Runnable onConfirm;
    private final Runnable onCancel;
    private final String confirmationMessage;

    public ConfirmationGui(String title, String confirmationMessage, ConfirmationAction onConfirm, CancelAction onCancel) {
        super(27, title);
        this.confirmationMessage = confirmationMessage;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
    }

    @Override
    public void buildGui() {
        addBook(4);

        addCancelItem(onCancel, 9);
        addCancelItem(onCancel, 10);
        addCancelItem(onCancel, 18);
        addCancelItem(onCancel, 19);

        addConfirmItem(onConfirm, 16);
        addConfirmItem(onConfirm, 17);
        addConfirmItem(onConfirm, 25);
        addConfirmItem(onConfirm, 26);
    }

    private void addBook(int slot) {
        ItemStack itemStack = Items.createBook("&6Confirm", confirmationMessage);
        setItem(slot, itemStack, null);
    }

    private void addConfirmItem(Runnable onConfirm, int slot) {
        ItemStack itemStack = Items.createGreenColoredGlass("Confirm","");
        setItem(slot, itemStack, new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                onConfirm.run();
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        });
    }

    private void addCancelItem(Runnable onCancel, int slot) {
        ItemStack itemStack = Items.createRedColoredGlass("Cancel", "");
        setItem(slot, itemStack, new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                onCancel.run();
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        });
    }
}
