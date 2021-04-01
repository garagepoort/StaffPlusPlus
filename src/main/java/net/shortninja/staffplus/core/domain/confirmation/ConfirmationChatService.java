package net.shortninja.staffplus.core.domain.confirmation;

import be.garagepoort.mcioc.IocBean;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IocBean
public class ConfirmationChatService {

    private static Map<UUID, ConfirmationAction> confirmationActions = new HashMap<>();
    private static Map<UUID, CancelAction> cancelActions = new HashMap<>();

    public void sendConfirmationMessage(Player player, String confirmationMessage, ConfirmationAction confirmationAction, CancelAction cancelAction) {
        UUID uuid = UUID.randomUUID();
        confirmationActions.put(uuid, confirmationAction);
        cancelActions.put(uuid, cancelAction);

        JSONMessage jsonMessage = JavaUtils.buildConfirmationMessage(confirmationMessage,
            "staffplus:confirm-action confirm " + uuid.toString(),
            "staffplus:confirm-action cancel " + uuid.toString());
        jsonMessage.send(player);
    }


    public void confirmAction(UUID uuid, Player player) {
        if(!confirmationActions.containsKey(uuid)) {
            throw new BusinessException("&CNo confirmation action found");
        }
        ConfirmationAction confirmationAction = confirmationActions.get(uuid);
        confirmationActions.remove(uuid);
        cancelActions.remove(uuid);

        confirmationAction.execute(player);
    }

    public void cancelAction(UUID uuid, Player player) {
        if(!cancelActions.containsKey(uuid)) {
            throw new BusinessException("&CNo cancel action found");
        }
        CancelAction cancelAction = cancelActions.get(uuid);
        confirmationActions.remove(uuid);
        cancelActions.remove(uuid);

        cancelAction.execute(player);
    }
}
