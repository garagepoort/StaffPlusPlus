package net.shortninja.staffplus.core.warnings.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.TimeUnitDurationTransformer;

import java.util.Optional;

public class WarningSeverityConfiguration {

    @ConfigProperty("name")
    private String name;
    @ConfigProperty("score")
    private int score;
    @ConfigProperty("expiresAfter")
    @ConfigTransformer(TimeUnitDurationTransformer.class)
    private long expirationDuration = -1;
    @ConfigProperty("reason")
    private String reason;
    @ConfigProperty("reasonOverwriteEnabled")
    private boolean reasonOverwriteEnabled;

    // Empty constructor for Tubing
    public WarningSeverityConfiguration(){}

    public WarningSeverityConfiguration(String name, int score, long expirationDuration, String reason, boolean reasonOverwriteEnabled) {
        this.name = name;
        this.score = score;
        this.expirationDuration = expirationDuration;
        this.reason = reason;
        this.reasonOverwriteEnabled = reasonOverwriteEnabled;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public long getExpirationDuration() {
        return expirationDuration;
    }

    public boolean isExpirationEnabled() {
        return expirationDuration > -1;
    }

    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }

    public boolean isReasonOverwriteEnabled() {
        return reasonOverwriteEnabled;
    }

    public boolean isReasonSettable() {
        return !getReason().isPresent() || isReasonOverwriteEnabled();
    }

    public boolean hasDefaultReason() {
        return getReason().isPresent();
    }
}
