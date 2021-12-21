package net.shortninja.staffplus.core.domain.staff.mute.database;

import net.shortninja.staffplus.core.domain.staff.mute.Mute;

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

    List<Mute> getMutesForPlayerPaged(UUID playerUuid, int offset, int amount);

    List<UUID> getAllPermanentMutedPlayers();

    long getTotalCount();

    long getActiveCount();

    Optional<Mute> getLastMute(UUID playerUuid);

    void setMuteDuration(int muteId, long duration);

    Optional<Mute> getMute(int muteId);

    List<Mute> getAppealedMutes(int offset, int amount);
}
