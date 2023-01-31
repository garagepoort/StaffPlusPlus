package net.shortninja.staffplus.core.punishments.ban.playerbans;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Optional;

public class PlayerRanks {

    private final List<String> ranks;
    private final PermissionHandler permission;

    public PlayerRanks(List<String> ranks, PermissionHandler permission) {
        this.ranks = ranks;
        this.permission = permission;
    }

    public boolean hasHigherRank(OfflinePlayer player1, OfflinePlayer player2) {
        Optional<String> player1Rank = getPlayerRank(player1);
        Optional<String> player2Rank = getPlayerRank(player2);
        if (player1Rank.isPresent() && player2Rank.isPresent()) {
            return ranks.indexOf(player1Rank.get()) > ranks.indexOf(player2Rank.get());
        }
        if (!player1Rank.isPresent() && player2Rank.isPresent()) {
            return false;
        }
        return true;
    }

    private Optional<String> getPlayerRank(OfflinePlayer player) {
        for (int i = ranks.size() - 1; i >= 0; i--) {
            if (permission.has(player, ranks.get(i))) {
                return Optional.ofNullable(ranks.get(i));
            }
        }
        return Optional.empty();
    }

    public boolean isEmpty() {
        return ranks.isEmpty();
    }
}
