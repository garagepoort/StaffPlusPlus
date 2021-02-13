package net.shortninja.staffplus.staff.warn.appeals.database;

import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.unordered.AppealStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppealRepository {

    List<Appeal> getAppeals(int warningId, int offset, int amount);

    void updateAppealStatus(int appealId, UUID resolverUuid, String resolveReason, AppealStatus status);

    Optional<Appeal> findAppeal(int appealId);

    void addAppeal(Appeal appeal);

    List<Appeal> getAppeals(int warningId);

    int getCountOpenAppeals();

    void deleteAppealsForWarning(int id);
}
