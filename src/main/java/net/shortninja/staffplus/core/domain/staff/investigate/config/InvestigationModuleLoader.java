package net.shortninja.staffplus.core.domain.staff.investigate.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.domain.actions.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@IocBean
public class InvestigationModuleLoader extends AbstractConfigLoader<InvestigationConfiguration> {

    @Override
    protected InvestigationConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("investigations-module.enabled");
        boolean investigatedTitleMessageEnabled = defaultConfig.getBoolean("investigations-module.notifications.investigated.title-message-enabled");
        boolean investigatedChatMessageEnabled = defaultConfig.getBoolean("investigations-module.notifications.investigated.chat-message-enabled");
        boolean allowOfflineInvestigation = defaultConfig.getBoolean("investigations-module.allow-offline-investigation");
        boolean automaticPause = defaultConfig.getBoolean("investigations-module.automatic-pause");
        boolean enforceStaffMode = defaultConfig.getBoolean("investigations-module.enforce-staff-mode");
        String staffMode = defaultConfig.getString("investigations-module.staff-mode");
        int maxConcurrentInvestigation = defaultConfig.getInt("investigations-module.max-concurrent-investigations", -1);
        String investigatePermission = permissionsConfig.getString("permissions.investigations.manage.investigate");
        String viewPermission = permissionsConfig.getString("permissions.investigations.manage.view");
        String linkEvidencePermission = permissionsConfig.getString("permissions.investigations.manage.link-evidence");
        String addNotePermission = permissionsConfig.getString("permissions.investigations.manage.add-note");
        String deleteNotePermission = permissionsConfig.getString("permissions.investigations.manage.delete-note");
        String deleteNoteOthersPermission = permissionsConfig.getString("permissions.investigations.manage.delete-note-others");
        String startInvestigationCmd = commandsConfig.getString("commands.investigations.manage.start");
        String pauseInvestigationCmd = commandsConfig.getString("commands.investigations.manage.pause");
        String concludeInvestigationCmd = commandsConfig.getString("commands.investigations.manage.conclude");
        String addNoteCmd = commandsConfig.getString("commands.investigations.manage.add-note");
        String commandManageInvestigationsGui = commandsConfig.getString("commands.investigations.manage.gui");
        String staffNotificationPermission = permissionsConfig.getString("permissions.investigations.manage.notifications");
        List<ConfiguredAction> startInvestigationCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) defaultConfig.getList("investigations-module.start-investigation-commands", new ArrayList<>()));
        List<ConfiguredAction> concludeInvestigationCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) defaultConfig.getList("investigations-module.conclude-investigation-commands", new ArrayList<>()));
        List<ConfiguredAction> pauseInvestigationCommands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) defaultConfig.getList("investigations-module.pause-investigation-commands", new ArrayList<>()));

        boolean modeGuiInvestigation = staffModeModulesConfig.getBoolean("modules.gui-module.investigation-gui");
        String modeGuiInvestigationTitle = staffModeModulesConfig.getString("modules.gui-module.investigation-title");
        String modeGuiInvestigationName = staffModeModulesConfig.getString("modules.gui-module.investigation-name");
        String modeGuiInvestigationLore = staffModeModulesConfig.getString("modules.gui-module.investigation-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiInvestigation, modeGuiInvestigationTitle, modeGuiInvestigationName, modeGuiInvestigationLore);

        return new InvestigationConfiguration(enabled,
            allowOfflineInvestigation,
            automaticPause,
            enforceStaffMode,
            staffMode,
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
            deleteNoteOthersPermission,
            guiItemConfig);
    }
}
