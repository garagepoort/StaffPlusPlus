package net.shortninja.staffplus.staff.kick.database;

import net.shortninja.staffplus.staff.kick.Kick;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface KicksRepository {

    int addKick(Kick kick);

    List<Kick> getKicksForPlayer(UUID playerUUID);

    Map<UUID, Integer> getCountByPlayer();
}
