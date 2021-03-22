package net.shortninja.staffplus.core.domain.confirmation;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.domain.staff.mode.item.ConfirmationType;
import org.bukkit.entity.Player;

import java.util.Map;

@IocBean
public class ConfirmationService {

    private final ConfirmationChatService confirmationChatService;

    public ConfirmationService(ConfirmationChatService confirmationChatService) {
        this.confirmationChatService = confirmationChatService;
    }

    public void showConfirmation(Player player, ConfirmationConfig confirmationConfig, Map<String, String> placeholders, ConfirmationAction confirmationAction, CancelAction cancelAction) {
        String confirmationMessage = confirmationConfig.getConfirmationMessage();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            confirmationMessage = confirmationMessage.replace(entry.getKey(), entry.getValue());
        }

        if (confirmationConfig.getConfirmationType() == ConfirmationType.GUI) {
            new ConfirmationGui("Confirm?", confirmationMessage, confirmationAction, cancelAction).show(player);
        } else {
            confirmationChatService.sendConfirmationMessage(player, confirmationMessage, confirmationAction, cancelAction);
        }
    }
}
