package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database;

import net.shortninja.staffplus.core.domain.staff.altaccountdetect.AltDetectWhitelistedItem;

import java.util.List;
import java.util.UUID;

public interface AltDetectWhitelistRepository {

    void addWhitelistedItem(UUID playerUuid1, UUID playerUuid2);

    void removeWhitelistedItem(UUID playerUuid1, UUID playerUuid2);

    List<AltDetectWhitelistedItem> getWhitelistedItems(UUID playerUuid1);

    List<AltDetectWhitelistedItem> getAllPAgedWhitelistedItems(int offset, int amount);
}
