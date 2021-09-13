package net.shortninja.staffplus.core.domain.delayedactions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.StoredCommandEntity;
import net.shortninja.staffplus.core.domain.actions.database.StoredCommandRepository;
import org.bukkit.entity.Player;

import java.util.List;

@IocBean
public class DelayedActionService {

    private final StoredCommandRepository storedCommandRepository;
    private final ActionService actionService;

    public DelayedActionService(StoredCommandRepository storedCommandRepository, ActionService actionService) {
        this.storedCommandRepository = storedCommandRepository;
        this.actionService = actionService;
    }

    public void processDelayedAction(Player player) {
        List<StoredCommandEntity> delayedActions = storedCommandRepository.getDelayedActions(player.getUniqueId());
        for (StoredCommandEntity delayedAction : delayedActions) {
            actionService.executeDelayed(delayedAction);
        }
    }
}
