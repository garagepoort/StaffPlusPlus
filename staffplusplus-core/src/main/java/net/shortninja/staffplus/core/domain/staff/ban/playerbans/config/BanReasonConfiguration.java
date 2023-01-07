package net.shortninja.staffplus.core.domain.staff.ban.playerbans.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;

import java.util.Optional;

public class BanReasonConfiguration {

    @ConfigProperty("name")
    private String name;
    @ConfigProperty("reason")
    private String reason;
    @ConfigProperty("template")
    private String template;
    @ConfigProperty("ban-type")
    @ConfigTransformer(ToEnum.class)
    private BanType banType;

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
