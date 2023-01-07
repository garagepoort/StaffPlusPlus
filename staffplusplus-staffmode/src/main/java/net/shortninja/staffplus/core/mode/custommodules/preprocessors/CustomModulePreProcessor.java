package net.shortninja.staffplus.core.mode.custommodules.preprocessors;

import net.shortninja.staffplus.core.mode.custommodules.CustomModuleConfiguration;
import net.shortninja.staffplus.core.mode.custommodules.CustomModuleExecutor;

import java.util.Map;

public interface CustomModulePreProcessor {

    CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders);
}
