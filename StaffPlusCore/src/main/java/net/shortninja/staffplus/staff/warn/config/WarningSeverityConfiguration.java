package net.shortninja.staffplus.staff.warn.config;

public class WarningSeverityConfiguration {

    private String name;
    private int score;

    public WarningSeverityConfiguration(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
