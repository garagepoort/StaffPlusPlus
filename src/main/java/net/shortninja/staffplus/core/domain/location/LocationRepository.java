package net.shortninja.staffplus.core.domain.location;

import org.bukkit.Location;

import java.util.Optional;

public interface LocationRepository {

    int addLocation(Location location);

    void removeLocation(int id);

    Optional<Location> findLocation(int id);

}
