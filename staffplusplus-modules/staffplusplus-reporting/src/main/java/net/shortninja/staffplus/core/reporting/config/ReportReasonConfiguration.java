package net.shortninja.staffplus.core.reporting.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import net.shortninja.staffplus.core.domain.actions.config.FiltersTransformer;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ReportReasonConfiguration {

    @ConfigProperty("reason")
    private String reason;

    @ConfigProperty("material")
    @ConfigTransformer(ToEnum.class)
    private Material material = Material.PAPER;

    @ConfigProperty("info")
    private String lore;

    @ConfigProperty("type")
    private String reportType;

    @ConfigProperty("filters")
    @ConfigTransformer(FiltersTransformer.class)
    private Map<String, String> filters = new HashMap<>();

    public String getReason() {
        return reason;
    }

    public Optional<String> getReportType() {
        return Optional.ofNullable(reportType);
    }

    public Material getMaterial() {
        return material;
    }

    public String getLore() {
        return lore;
    }

    public boolean filterMatches(Predicate<Map<String, String>>[] predicate) {
        return Arrays.stream(predicate).allMatch(p -> p.test(filters));
    }
}
