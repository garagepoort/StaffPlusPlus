package net.shortninja.staffplus.staff.warn.warnings.config;

public class WarningSeverityConfiguration {

    private String name;
    private int score;
    private long expirationDuration;

    public WarningSeverityConfiguration(String name, int score, long expirationDuration) {
        this.name = name;
        this.score = score;
        this.expirationDuration = expirationDuration;
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
}
