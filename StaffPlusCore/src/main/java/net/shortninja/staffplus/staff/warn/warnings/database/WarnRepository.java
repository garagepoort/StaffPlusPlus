package net.shortninja.staffplus.staff.warn.warnings.database;

import net.shortninja.staffplus.staff.warn.warnings.Warning;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    Optional<Warning> findWarning(int warningId);
}
