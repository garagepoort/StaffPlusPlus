package net.shortninja.staffplus.common.placeholder;

import net.shortninja.staffplusplus.placeholders.PlaceholderDataProvider;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.OfflinePlayer;

import java.util.Optional;

public class PlaceholderDataProviderImpl implements PlaceholderDataProvider {
    @Override
    public Optional<Integer> totalAmountOfWarningsForPlayer(OfflinePlayer offlinePlayer, boolean includeExpired, boolean includeAppealed) {
        return Optional.of(0);
    }

    @Override
    public Optional<Integer> totalAmountOfReportsForPlayer(OfflinePlayer offlinePlayer, ReportStatus... reportStatuses) {
        return Optional.of(0);
    }
}
