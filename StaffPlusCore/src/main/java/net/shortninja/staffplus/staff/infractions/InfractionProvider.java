package net.shortninja.staffplus.staff.infractions;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InfractionProvider {

    List<? extends Infraction> getInfractions(Player executor, UUID playerUUID);

    Optional<InfractionCount> getInfractionsCount();

    InfractionType getType();
}
