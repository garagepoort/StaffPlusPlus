package net.shortninja.staffplus.core.domain.confirmation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.Items;
import org.bukkit.inventory.ItemStack;

@IocBean
public class ConfirmationViewBuilder {
    private final Messages messages;

    public ConfirmationViewBuilder(Messages messages) {
        this.messages = messages;
    }

    public TubingGui buildGui(String title, String confirmationMessage, String confirmAction, String cancelAction) {
        TubingGui.Builder builder  = new TubingGui.Builder(messages.colorize(title), 27);

        builder.addItem(TubingGuiActions.NOOP,4, Items.createBook("&6Confirm", confirmationMessage));

        ItemStack cancelItem = Items.createRedColoredGlass("Cancel", "");
        builder.addItem(cancelAction, 9, cancelItem);
        builder.addItem(cancelAction, 10, cancelItem);
        builder.addItem(cancelAction, 18, cancelItem);
        builder.addItem(cancelAction, 19, cancelItem);

        ItemStack confirmItem = Items.createGreenColoredGlass("Confirm","");
        builder.addItem(confirmAction, 16, confirmItem);
        builder.addItem(confirmAction, 17, confirmItem);
        builder.addItem(confirmAction, 25, confirmItem);
        builder.addItem(confirmAction, 26, confirmItem);

        return builder.build();
    }
}
