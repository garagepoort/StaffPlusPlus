package net.shortninja.staffplus.core.domain.staff.infractions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

@IocBean
public class InfractionsService {

    private final List<InfractionProvider> infractionProviders;
    private final PlayerManager playerManager;

    public InfractionsService(@IocMulti(InfractionProvider.class) List<InfractionProvider> infractionProviders, PlayerManager playerManager) {
        this.infractionProviders = infractionProviders;
        this.playerManager = playerManager;
    }

    public List<Infraction> getAllInfractions(Player executor, UUID playerUuid, int page, int pageSize) {
        List<Infraction> infractions = new ArrayList<>();
        for (InfractionProvider infractionProvider : infractionProviders) {
            infractions.addAll(infractionProvider.getInfractions(executor, playerUuid));
        }
        infractions.sort(Comparator.comparingLong(Infraction::getCreationTimestamp).reversed());
        return JavaUtils.getPageOfList(infractions, page, pageSize);
    }

    public List<InfractionOverview> getTopInfractions(int page, int pageSize, List<InfractionType> infractionFilters) {
        List<InfractionOverview> infractions = new ArrayList<>();

        infractionProviders.stream()
            .filter(infractionProvider -> infractionFilters.isEmpty() || infractionFilters.contains(infractionProvider.getType()))
            .map(InfractionProvider::getInfractionsInfo)
            .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
            .forEach(infractionInfo -> addInfractionOverview(infractions, infractionInfo));

        infractions.sort(Comparator.comparingInt(InfractionOverview::getTotal).reversed());
        return JavaUtils.getPageOfList(infractions, page, pageSize);
    }

    private void addInfractionOverview(List<InfractionOverview> infractions, InfractionInfo infractionInfo) {
        infractionInfo.getCounts().keySet().forEach(uuid -> addInfractionOverview(infractions, infractionInfo, uuid));
    }

    private void addInfractionOverview(List<InfractionOverview> infractions, InfractionInfo infractionsCount, UUID uuid) {
        Optional<InfractionOverview> existingOverviewItem = infractions.stream().filter(overview -> overview.getSppPlayer().getId().equals(uuid)).findFirst();

        if (!existingOverviewItem.isPresent()) {
            Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(uuid);
            if (!onOrOfflinePlayer.isPresent()) {
                return;
            }
            existingOverviewItem = Optional.of(new InfractionOverview());
            existingOverviewItem.get().setSppPlayer(onOrOfflinePlayer.get());
            infractions.add(existingOverviewItem.get());
        }

        existingOverviewItem.get().getInfractions().put(infractionsCount.getInfractionType(), infractionsCount.getCounts().get(uuid));
        if (infractionsCount.getAdditionalInfo().containsKey(uuid)) {
            existingOverviewItem.get().getAdditionalInfo().addAll(infractionsCount.getAdditionalInfo().get(uuid));
        }

    }

}
