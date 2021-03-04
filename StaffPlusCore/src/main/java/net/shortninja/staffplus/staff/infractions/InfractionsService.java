package net.shortninja.staffplus.staff.infractions;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.common.JavaUtils;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

public class InfractionsService {

    private final List<InfractionProvider> infractionProviders;
    private final PlayerManager playerManager;

    public InfractionsService(List<InfractionProvider> infractionProviders, PlayerManager playerManager) {
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
            .map(InfractionProvider::getInfractionsCount)
            .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
            .forEach(infractionCount -> {
                infractionCount.getCounts().forEach((uuid, count) -> addInfractionOverview(infractions, infractionCount, uuid));
            });

        infractions.sort(Comparator.comparingInt(InfractionOverview::getTotal).reversed());
        return JavaUtils.getPageOfList(infractions, page, pageSize);
    }

    private void addInfractionOverview(List<InfractionOverview> infractions, InfractionCount infractionsCount, UUID uuid) {
        Optional<InfractionOverview> infractionOverview = infractions.stream()
            .filter(overview -> overview.getSppPlayer().getId().equals(uuid)).findFirst();
        if (!infractionOverview.isPresent()) {
            infractionOverview = Optional.of(new InfractionOverview());
            Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(uuid);
            if (!onOrOfflinePlayer.isPresent()) {
                return;
            }
            infractionOverview.get().setSppPlayer(onOrOfflinePlayer.get());
            infractions.add(infractionOverview.get());
        }
        infractionOverview.get().getInfractions().put(infractionsCount.getInfractionType(), infractionsCount.getCounts().get(uuid));

    }

}
