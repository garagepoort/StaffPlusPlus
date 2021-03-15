package net.shortninja.staffplus.staff.mode.handler;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.util.Message;

public class InputCustomModulePreprocessor implements CustomModulePreProcessor {

    private static final String CANCEL = "cancel";
    private final Message messageCoordinator;
    private final Messages messages;

    public InputCustomModulePreprocessor(Message messageCoordinator, Messages messages) {
        this.messageCoordinator = messageCoordinator;
        this.messages = messages;
    }

    @Override
    public CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration) {
        if (!customModuleConfiguration.isRequireInput()) {
            return action;
        }
        return player -> {
            PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());
            messageCoordinator.send(player, "&7------", messages.prefixGeneral);
            messageCoordinator.send(player, "&6" + customModuleConfiguration.getInputPrompt(), messages.prefixGeneral);
            messageCoordinator.send(player, "&7------", messages.prefixGeneral);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messageCoordinator.send(player, "&CYou have cancelled this action", messages.prefixGeneral);
                    return;
                }
                action.execute(player);
            });
        };
    }

}
