package net.shortninja.staffplus.staff.ban.database;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.ban.Ban;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BansRepository {

    int addBan(Ban ban);

    List<Ban> getBans(int offset, int amount);

    void unban(int id, UUID unban_uuid, String unbanReason);

    void unban(SppPlayer player, UUID unban_uuid, String unbanReason);

    Optional<Ban> findActiveBan(UUID playerUuid);

    Optional<Ban> findActiveBan(int id);
}
