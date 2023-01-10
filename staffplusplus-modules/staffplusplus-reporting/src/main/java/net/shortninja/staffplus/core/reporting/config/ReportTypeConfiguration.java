package net.shortninja.staffplus.core.reporting.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import net.shortninja.staffplus.core.domain.actions.config.FiltersTransformer;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ReportTypeConfiguration {

    @ConfigProperty(value = "name", required = true, error = "Invalid report type configuration. Name is required")
    private String type;

    @ConfigProperty("material")
    @ConfigTransformer(ToEnum.class)
    private Material material = Material.PAPER;

    @ConfigProperty("info")
    private String lore;

    @ConfigProperty("filters")
    @ConfigTransformer(FiltersTransformer.class)
    private Map<String, String> filters = new HashMap<>();

    public String getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public String getLore() {
        return lore;
    }

    @SafeVarargs
    public final boolean filterMatches(Predicate<Map<String, String>>... predicate) {
        return Arrays.stream(predicate).allMatch(p -> p.test(filters));
    }
}
