package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.common.time.TimeUnit;

public class TimeUnitDurationTransformer implements IConfigTransformer<Long, String> {

    @Override
    public Long mapConfig(String s) {
        return TimeUnit.getDuration(s);
    }
}
