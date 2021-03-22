package net.shortninja.staffplus.core.domain.staff.mode.handler;

import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;

import java.util.Map;

public interface CustomModulePreProcessor {

    CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders);
}
