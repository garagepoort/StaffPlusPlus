package net.shortninja.staffplus.common.confirmation;

import net.shortninja.staffplus.staff.mode.item.ConfirmationType;
import org.bukkit.entity.Player;

public class ConfirmationService {

    private final ConfirmationChatService confirmationChatService;

    public ConfirmationService(ConfirmationChatService confirmationChatService) {
        this.confirmationChatService = confirmationChatService;
    }

    public void showConfirmation(Player player, ConfirmationConfig confirmationConfig, ConfirmationAction confirmationAction, CancelAction cancelAction) {
        if (confirmationConfig.getConfirmationType() == ConfirmationType.GUI) {
            new ConfirmationGui("Confirm?", confirmationConfig.getConfirmationMessage(), confirmationAction, cancelAction).show(player);
        } else {
            confirmationChatService.sendConfirmationMessage(player, confirmationConfig.getConfirmationMessage(), confirmationAction, cancelAction);
        }
    }
}
