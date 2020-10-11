package net.shortninja.staffplus.staff.altaccountdetect.database.whitelist;

import net.shortninja.staffplus.staff.altaccountdetect.AltCheckWhitelistedItem;

import java.util.List;
import java.util.UUID;

public interface AltDetectWhitelistRepository {

    void addWhitelistedItem(UUID playerUuid1, UUID playerUuid2);

    void removeWhitelistedItem(UUID playerUuid1, UUID playerUuid2);

    List<AltCheckWhitelistedItem> getWhitelistedItems(UUID playerUuid1);
}
