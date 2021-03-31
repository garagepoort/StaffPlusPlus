package net.shortninja.staffplus.core.domain.staff.investigate.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class InvestigationModuleLoader extends AbstractConfigLoader<InvestigationConfiguration> {

    @Override
    protected InvestigationConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("investigations-module.enabled");
        boolean titleMessageEnabled = config.getBoolean("investigations-module.title-message-enabled");
        String investigatePermission = config.getString("permissions.investigations.manage.investigate");
        String startInvestigationCmd = config.getString("commands.investigations.manage.start");
        String pauseInvestigationCmd = config.getString("commands.investigations.manage.pause");
        String concludeInvestigationCmd = config.getString("commands.investigations.manage.conclude");
        String staffNotificationPermission = config.getString("permissions.investigations.manage.notifications");

        return new InvestigationConfiguration(enabled,
            investigatePermission,
            startInvestigationCmd,
            pauseInvestigationCmd,
            concludeInvestigationCmd,
            titleMessageEnabled, staffNotificationPermission);
    }
}
