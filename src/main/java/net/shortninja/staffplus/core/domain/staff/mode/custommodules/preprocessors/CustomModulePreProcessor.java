package net.shortninja.staffplus.core.domain.staff.mode.custommodules.preprocessors;

import net.shortninja.staffplus.core.domain.staff.mode.custommodules.CustomModuleConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.CustomModuleExecutor;

import java.util.Map;

public interface CustomModulePreProcessor {

    CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders);
}
