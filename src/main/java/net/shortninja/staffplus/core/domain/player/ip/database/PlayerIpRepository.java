package net.shortninja.staffplus.core.domain.player.ip.database;

import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerIpRepository {

    List<String> getIps(UUID playerUuid);

    List<PlayerIpRecord> getAllIpRecords();

    void save(UUID playerUuid, String playerName, String ip, String server);

    Optional<String> getLastIp(UUID playerUuid);

    List<PlayerIpRecord> findInSubnet(long lower, long upper);

    List<PlayerIpRecord> findWithIp(long ip);

    void deleteRecordsFor(SppPlayer player);
}
