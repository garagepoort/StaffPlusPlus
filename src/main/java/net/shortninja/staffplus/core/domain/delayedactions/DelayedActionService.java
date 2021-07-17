package net.shortninja.staffplus.core.domain.delayedactions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.delayedactions.database.DelayedActionsRepository;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class DelayedActionService {

    private final DelayedActionsRepository delayedActionsRepository;

    public DelayedActionService(DelayedActionsRepository delayedActionsRepository) {
        this.delayedActionsRepository = delayedActionsRepository;
    }

    public void processDelayedAction(Player player) {
        List<DelayedAction> delayedActions = delayedActionsRepository.getDelayedActions(player.getUniqueId());
        sendEvent(new DelayedActionsExecutedEvent(player, delayedActions));
    }
}
