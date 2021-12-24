package net.shortninja.staffplus.core.domain.staff.appeals.database;

import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppealRepository {

    List<Appeal> getAppeals(int appealableId, int offset, int amount);

    void updateAppealStatus(int appealId, UUID resolverUuid, String resolveReason, AppealStatus status, AppealableType appealableType);

    Optional<Appeal> findAppeal(int appealId);

    void addAppeal(Appeal appeal, AppealableType appealableType);

    List<Appeal> getAppeals(int appealableId, AppealableType appealableType);

    Optional<Appeal> findAppeal(int appealableId, AppealableType type);

    int getCountOpenAppeals(AppealableType appealableType, String syncTable, ServerSyncConfig syncConfig);

    void deleteAppeals(int appealableId, AppealableType appealableType);
}
