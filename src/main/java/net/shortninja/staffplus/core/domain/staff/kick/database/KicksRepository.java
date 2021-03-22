package net.shortninja.staffplus.core.domain.staff.kick.database;

import net.shortninja.staffplus.core.domain.staff.kick.Kick;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface KicksRepository {

    int addKick(Kick kick);

    List<Kick> getKicksForPlayer(UUID playerUUID);

    Map<UUID, Integer> getCountByPlayer();
}
