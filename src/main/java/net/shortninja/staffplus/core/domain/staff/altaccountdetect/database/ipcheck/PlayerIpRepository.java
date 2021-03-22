package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck;

import java.util.List;
import java.util.UUID;

public interface PlayerIpRepository {

    List<String> getIps(UUID playerUuid);

    void save(UUID playerUuid, String ip);
}
