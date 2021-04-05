package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;
import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.OPTIONAL;

@IocBean
public class CommandPlayerRetriever {

    private final PlayerManager playerManager;
    private final Messages messages;

    public CommandPlayerRetriever(PlayerManager playerManager, Messages messages) {
        this.playerManager = playerManager;
        this.messages = messages;
    }

    public Optional<SppPlayer> retrievePlayer(PlayerRetrievalStrategy strategy, String playerName, boolean delayed) {
        if (strategy == NONE) {
            return Optional.empty();
        }

        if (strategy == OPTIONAL && playerName == null) {
            return Optional.empty();
        }

        SppPlayer player = playerManager.getOnOrOfflinePlayer(playerName).orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

        switch (strategy) {
            case BOTH:
                return Optional.of(player);
            case ONLINE:
                if (!player.isOnline() && !delayed) {
                    throw new PlayerOfflineException();
                }
                return Optional.of(player);
            default:
                return Optional.empty();
        }
    }
}
