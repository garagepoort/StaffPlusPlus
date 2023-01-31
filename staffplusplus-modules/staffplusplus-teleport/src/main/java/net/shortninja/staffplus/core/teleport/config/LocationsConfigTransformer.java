package net.shortninja.staffplus.core.teleport.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;

public class LocationsConfigTransformer implements IConfigTransformer<Map<String, Location>, List<LinkedHashMap<String, String>>> {

    @Override
    public Map<String, Location> mapConfig(List<LinkedHashMap<String, String>> linkedHashMaps) {
        Map<String, Location> locations = new HashMap<>();
        linkedHashMaps.forEach(map -> {
            String name = map.get("name");
            String coordinates = map.get("coordinates");
            String[] points = coordinates.split(";");
            if (points.length < 3) {
                throw new ConfigurationException("Invalid locations configuration. Make sure your location points are in format x;y;z;worldname");
            }

            World world = points.length == 4 ? Bukkit.getWorld(points[3]) : Bukkit.getWorlds().get(0);
            Location location = new Location(world, parseDouble(points[0]), parseDouble(points[1]), parseDouble(points[2]));
            locations.put(name, location);
        });
        return locations;
    }
}
