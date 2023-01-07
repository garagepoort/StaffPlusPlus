package net.shortninja.staffplus.core.domain.location;

import org.bukkit.Location;

import java.util.Optional;

public interface LocationRepository {

    int addLocation(SppLocation location);

    int addLocation(Location location);

    void removeLocation(int id);

    Optional<SppLocation> findLocation(int id);

    void updateLocation(SppLocation sppLocation);
}
