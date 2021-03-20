package net.shortninja.staffplus.domain.staff.mode.handler;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.Bukkit;

import java.util.Map;

public class InputCustomModulePreprocessor implements CustomModulePreProcessor {

    private static final String CANCEL = "cancel";
    private final MessageCoordinator messageCoordinator;
    private final Messages messages;

    public InputCustomModulePreprocessor(MessageCoordinator messageCoordinator, Messages messages) {
        this.messageCoordinator = messageCoordinator;
        this.messages = messages;
    }

    @Override
    public CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders) {
        if (!customModuleConfiguration.isRequireInput()) {
            return action;
        }
        return (player, pl) -> {
            String inputPrompt = customModuleConfiguration.getInputPrompt();
            for (Map.Entry<String, String> entry : pl.entrySet()) {
                inputPrompt = inputPrompt.replace(entry.getKey(), entry.getValue());
            }

            PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());
            messageCoordinator.send(player, "&7------", messages.prefixGeneral);
            messageCoordinator.send(player, "&6" + inputPrompt, messages.prefixGeneral);
            messageCoordinator.send(player, "&6 or \"cancel\" to cancel the action", messages.prefixGeneral);
            messageCoordinator.send(player, "&7------", messages.prefixGeneral);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messageCoordinator.send(player, "&CYou have cancelled this action", messages.prefixGeneral);
                    return;
                }
                pl.put("%input%", message);
                Bukkit.getScheduler().runTaskLater(StaffPlus.get(), () -> action.execute(player, pl), 1);
            });
        };
    }

}
