package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import java.util.Optional;

public class WarningSeverityConfiguration {

    private String name;
    private int score;
    private long expirationDuration;
    private String reason;
    private boolean reasonOverwriteEnabled;

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
