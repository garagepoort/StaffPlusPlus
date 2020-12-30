package net.shortninja.staffplus.staff.ban.database;

import net.shortninja.staffplus.staff.ban.Ban;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BansRepository {

    int addBan(Ban ban);

    List<Ban> getActiveBans(int offset, int amount);

    void update(Ban ban);

    Optional<Ban> findActiveBan(UUID playerUuid);

    Optional<Ban> findActiveBan(int id);

    List<Ban> getBansForPlayer(UUID playerUUID);
}
