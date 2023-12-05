package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.IocBean;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean
public class JoinMessageService implements net.shortninja.staffplusplus.joinmessages.JoinMessageService {

    private JoinMessagesConfiguration joinMessagesConfiguration;

    public JoinMessageService(JoinMessagesConfiguration joinMessagesConfiguration) {
        this.joinMessagesConfiguration = joinMessagesConfiguration;
    }

    @Override
    public Optional<JoinMessageGroup> getJoinMessageGroup(Player player) {
        return joinMessagesConfiguration.getJoinMessageGroup(player);
    }

}
