package net.shortninja.staffplus.core.domain.staff.investigate.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@IocBean
public class InvestigationModuleLoader extends AbstractConfigLoader<InvestigationConfiguration> {

    @Override
    protected InvestigationConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("investigations-module.enabled");
        boolean investigatedTitleMessageEnabled = config.getBoolean("investigations-module.notifications.investigated.title-message-enabled");
        boolean investigatedChatMessageEnabled = config.getBoolean("investigations-module.notifications.investigated.chat-message-enabled");
        boolean allowOfflineInvestigation = config.getBoolean("investigations-module.allow-offline-investigation");
        int maxConcurrentInvestigation = config.getInt("investigations-module.max-concurrent-investigations", -1);
        String investigatePermission = config.getString("permissions.investigations.manage.investigate");
        String viewPermission = config.getString("permissions.investigations.manage.view");
        String linkEvidencePermission = config.getString("permissions.investigations.manage.link-evidence");
        String addNotePermission = config.getString("permissions.investigations.manage.add-note");
        String deleteNotePermission = config.getString("permissions.investigations.manage.delete-note");
        String deleteNoteOthersPermission = config.getString("permissions.investigations.manage.delete-note-others");
        String startInvestigationCmd = config.getString("commands.investigations.manage.start");
        String pauseInvestigationCmd = config.getString("commands.investigations.manage.pause");
        String concludeInvestigationCmd = config.getString("commands.investigations.manage.conclude");
        String addNoteCmd = config.getString("commands.investigations.manage.add-note");
        String commandManageInvestigationsGui = config.getString("commands.investigations.manage.gui");
        String staffNotificationPermission = config.getString("permissions.investigations.manage.notifications");
        List<ConfiguredAction> startInvestigationCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) config.getList("investigations-module.start-investigation-commands", new ArrayList<>()));
        List<ConfiguredAction> concludeInvestigationCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) config.getList("investigations-module.conclude-investigation-commands", new ArrayList<>()));
        List<ConfiguredAction> pauseInvestigationCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) config.getList("investigations-module.pause-investigation-commands", new ArrayList<>()));

        return new InvestigationConfiguration(enabled,
            allowOfflineInvestigation,
            maxConcurrentInvestigation,
            investigatePermission,
            startInvestigationCmd,
            pauseInvestigationCmd,
            concludeInvestigationCmd,
            addNoteCmd,
            investigatedTitleMessageEnabled,
            investigatedChatMessageEnabled,
            staffNotificationPermission,
            startInvestigationCommands,
            concludeInvestigationCommands,
            pauseInvestigationCommands,
            commandManageInvestigationsGui,
            viewPermission,linkEvidencePermission,
            addNotePermission,
            deleteNotePermission,
            deleteNoteOthersPermission);
    }
}
