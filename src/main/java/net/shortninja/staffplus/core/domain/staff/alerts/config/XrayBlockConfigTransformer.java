package net.shortninja.staffplus.core.domain.staff.alerts.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayBlockConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class XrayBlockConfigTransformer implements IConfigTransformer<List<XrayBlockConfig>, String> {
    @Override
    public List<XrayBlockConfig> mapConfig(String s) {
        if(StringUtils.isBlank(s)) {
            return Collections.emptyList();
        }
        return Arrays.stream(s.split("\\s*,\\s*"))
            .map(XrayBlockConfig::new)
            .collect(Collectors.toList());
    }
}
