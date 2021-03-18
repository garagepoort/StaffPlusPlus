package net.shortninja.staffplus.staff.mute.database;

import net.shortninja.staffplus.staff.mute.Mute;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MuteRepository {

    int addMute(Mute mute);

    List<Mute> getActiveMutes(int offset, int amount);

    List<Mute> getMutesForPlayer(UUID playerUuid);

    Map<UUID, Long> getMuteDurationByPlayer();

    void update(Mute mute);

    Optional<Mute> findActiveMute(UUID playerUuid);

    List<Mute> getAllActiveMutes(List<String> playerUuids);

    Optional<Mute> findActiveMute(int id);

    Map<UUID, Integer> getCountByPlayer();

    List<UUID> getAllPermanentMutedPlayers();
}
