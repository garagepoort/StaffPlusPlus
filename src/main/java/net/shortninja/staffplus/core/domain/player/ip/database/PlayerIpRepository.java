package net.shortninja.staffplus.core.domain.player.ip.database;

import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerIpRepository {

    List<String> getIps(UUID playerUuid);

    List<PlayerIpRecord> getAllIpRecords();

    void save(UUID playerUuid, String playerName, String ip);

    void delete(UUID playerUuid, String ip);

    Optional<String> getLastIp(UUID playerUuid);

    List<PlayerIpRecord> findInSubnet(long lower, long upper);

    List<PlayerIpRecord> findWithIp(long ip);
}
