package net.shortninja.staffplus.staff.mode.handler;

import net.shortninja.staffplus.staff.mode.item.CustomModuleConfiguration;

import java.util.Map;

public interface CustomModulePreProcessor {

    CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders);
}
