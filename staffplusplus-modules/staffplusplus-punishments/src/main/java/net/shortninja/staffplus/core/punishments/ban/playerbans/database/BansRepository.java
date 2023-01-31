package net.shortninja.staffplus.core.punishments.ban.playerbans.database;

import net.shortninja.staffplus.core.punishments.ban.playerbans.Ban;
import net.shortninja.staffplusplus.ban.BanFilters;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BansRepository {

    int addBan(Ban ban);

    List<Ban> getActiveBans(int offset, int amount);

    void update(Ban ban);

    Optional<Ban> findActiveBan(UUID playerUuid);

    Optional<Ban> findActiveBan(int id);

    Optional<Ban> findBan(String target, long creationTimestamp);

    Optional<Ban> getBan(int id);

    List<Ban> getBansForPlayer(UUID playerUUID);

    List<Ban> getBansForPlayerPaged(UUID playerUUID, int offset, int amount);

    Map<UUID, Integer> getCountByPlayer();

    List<UUID> getAllPermanentBannedPlayers();

    long getActiveCount();

    Map<UUID, Long> getBanDurationByPlayer();

    long getTotalCount();

    void setBanDuration(int banId, long duration);

    List<Ban> getAppealedBans(int offset, int amount);

    long getBanCount(BanFilters banFilters);
}
