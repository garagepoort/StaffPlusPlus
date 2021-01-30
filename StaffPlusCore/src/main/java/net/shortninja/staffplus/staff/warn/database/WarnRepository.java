package net.shortninja.staffplus.staff.warn.database;

import net.shortninja.staffplus.staff.warn.Warning;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WarnRepository {

    List<Warning> getWarnings(UUID uuid);

    List<Warning> getWarnings();

    int getTotalScore(UUID uuid);

    void addWarning(Warning warning);

    void removeWarnings(UUID uuid);

    void removeWarning(int id);

    List<Warning> getWarnings(UUID uniqueId, int offset, int amount);

    void markWarningsRead(UUID uniqueId);

    Map<UUID, Integer> getCountByPlayer();
}
