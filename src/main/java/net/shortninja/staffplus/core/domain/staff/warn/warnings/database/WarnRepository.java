package net.shortninja.staffplus.core.domain.staff.warn.warnings.database;

import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface WarnRepository {

    List<Warning> getWarnings(UUID uuid);

    List<Warning> getWarnings();

    int getTotalScore(UUID uuid);

    int addWarning(Warning warning);

    void removeWarnings(UUID uuid);

    void removeWarning(int id);

    void expireWarnings(String name, long timestamp);

    void expireWarning(int id);

    List<Warning> getWarnings(UUID uniqueId, int offset, int amount);

    List<Warning> getAllWarnings(int offset, int amount);

    void markWarningsRead(UUID uniqueId);

    Map<UUID, Integer> getCountByPlayer();

    Optional<Warning> findWarning(int warningId);

    List<Warning> getAppealedWarnings(int offset, int amount);
}
