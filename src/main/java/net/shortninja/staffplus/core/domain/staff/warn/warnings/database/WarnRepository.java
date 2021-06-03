package net.shortninja.staffplus.core.domain.staff.warn.warnings.database;

import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.warnings.WarningFilters;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface WarnRepository {

    List<Warning> getWarnings(UUID uuid);

    List<Warning> getWarnings();

    int getTotalScore(UUID uuid);

    int getTotalScore(String playerName);

    int addWarning(Warning warning);

    void removeWarning(int id);

    void expireWarnings(String name, long timestamp);

    void expireWarning(int id);

    List<Warning> getWarnings(UUID uniqueId, int offset, int amount);

    List<Warning> getAllWarnings(int offset, int amount);

    List<Warning> findWarnings(WarningFilters warningFilters, int offset, int amount);

    void markWarningsRead(UUID uniqueId);

    Map<UUID, Integer> getCountByPlayer();

    Optional<Warning> findWarning(int warningId);

    List<Warning> getAppealedWarnings(int offset, int amount);

    long getWarnCount(WarningFilters warningFilters);
}
