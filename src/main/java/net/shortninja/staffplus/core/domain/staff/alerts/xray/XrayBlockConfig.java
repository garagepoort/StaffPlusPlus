package net.shortninja.staffplus.core.domain.staff.alerts.xray;

import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.common.time.TimeUnitShort;
import org.bukkit.Material;

public class XrayBlockConfig {

    private Material material;
    private Long duration;
    private Integer amountOfBlocks;

    public XrayBlockConfig(String config) {
        String[] split = config.split(":");
        if(split.length > 3) {
            throw new ConfigurationException("Bad xray block configuration");
        }

        if(split.length == 3) {
            String amountString = split[2].substring(0, split[2].length() - 1);
            String timeUnit = split[2].substring(split[2].length() - 1);
            int amount = Integer.parseInt(amountString);
            this.duration = TimeUnitShort.getDuration(timeUnit, amount);
        }
        if(split.length >= 2) {
            amountOfBlocks = Integer.parseInt(split[1]);
        }

        material = Options.stringToMaterial(split[0]);
    }

    public Material getMaterial() {
        return material;
    }

    public Long getDuration() {
        return duration;
    }

    public Integer getAmountOfBlocks() {
        return amountOfBlocks;
    }
}
