package net.shortninja.staffplus.core.domain.staff.teleport.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import org.bukkit.Location;

import java.util.Map;

@IocBean
public class TeleportConfiguration {

    @ConfigProperty("locations")
    @ConfigTransformer(LocationsConfigTransformer.class)
    public Map<String, Location> locations;
}
