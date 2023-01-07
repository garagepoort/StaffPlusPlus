package net.shortninja.staffplus.core.domain.staff.alerts.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AltDetectTrustLevelConfigTransformer implements IConfigTransformer<List<AltDetectTrustLevel>, String> {
    @Override
    public List<AltDetectTrustLevel> mapConfig(String s) {
        if(StringUtils.isBlank(s)) {
            return Collections.emptyList();
        }
        return Arrays.stream(s.split(";"))
            .map(AltDetectTrustLevel::valueOf)
            .collect(Collectors.toList());
    }
}
