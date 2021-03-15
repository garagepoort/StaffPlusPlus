package net.shortninja.staffplus.staff.mode.handler;

import net.shortninja.staffplus.staff.mode.item.CustomModuleConfiguration;

public interface CustomModulePreProcessor {

    CustomModuleExecutor process(CustomModuleExecutor action, CustomModuleConfiguration customModuleConfiguration);
}
