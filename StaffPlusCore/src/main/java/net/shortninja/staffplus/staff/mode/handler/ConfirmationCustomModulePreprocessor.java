package net.shortninja.staffplus.staff.mode.handler;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.confirmation.ConfirmationConfig;
import net.shortninja.staffplus.common.confirmation.ConfirmationService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.util.Message;
import org.bukkit.Bukkit;

public class ConfirmationCustomModulePreprocessor implements CustomModulePreProcessor {

    private final Messages messages;
    private final ConfirmationService confirmationService;
    private final Message message;

    public ConfirmationCustomModulePreprocessor(Messages messages, ConfirmationService confirmationService, Message message) {
        this.messages = messages;
        this.confirmationService = confirmationService;
        this.message = message;
    }

    @Override
    public CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration) {
        if (!customModuleConfiguration.getConfirmationConfig().isPresent()) {
            return action;
        }

        return player -> Bukkit.getScheduler().runTaskLater(StaffPlus.get(), () -> {
            ConfirmationConfig confirmationConfig = customModuleConfiguration.getConfirmationConfig().get();
            confirmationService.showConfirmation(player, confirmationConfig,
                action::execute,
                p -> message.send(p, "You have cancelled the action", messages.prefixGeneral));
        }, 1);
    }

}
