package net.shortninja.staffplus.core.examine.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;

public class ExamineModeItemLocationConfigTransformer implements IConfigTransformer<Integer, Integer> {
    @Override
    public Integer mapConfig(Integer value) {
        return value <= 0 ? -1 : value;
    }
}
