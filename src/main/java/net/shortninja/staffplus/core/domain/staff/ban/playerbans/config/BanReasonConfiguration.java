package net.shortninja.staffplus.core.domain.staff.ban.playerbans.config;

import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;

import java.util.Optional;

public class BanReasonConfiguration {

    private String name;
    private String reason;
    private String template;
    private BanType banType;

    public BanReasonConfiguration(String name, String reason, String template, BanType banType) {
        this.name = name;
        this.reason = reason;
        this.template = template;
        this.banType = banType;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public Optional<String> getTemplate() {
        return Optional.ofNullable(template);
    }

    public Optional<BanType> getBanType() {
        return Optional.ofNullable(banType);
    }
}
