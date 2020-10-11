package net.shortninja.staffplus.staff.altaccountdetect.database.whitelist;

import net.shortninja.staffplus.staff.altaccountdetect.AltDetectWhitelistedItem;

import java.util.List;
import java.util.UUID;

public interface AltDetectWhitelistRepository {

    void addWhitelistedItem(UUID playerUuid1, UUID playerUuid2);

    void removeWhitelistedItem(UUID playerUuid1, UUID playerUuid2);

    List<AltDetectWhitelistedItem> getWhitelistedItems(UUID playerUuid1);

    List<AltDetectWhitelistedItem> getAllPAgedWhitelistedItems(int offset, int amount);
}
