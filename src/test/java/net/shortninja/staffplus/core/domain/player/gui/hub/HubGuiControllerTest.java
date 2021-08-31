package net.shortninja.staffplus.core.domain.player.gui.hub;

import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.ColorViewBuilder;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HubGuiControllerTest extends AbstractGuiTemplateTest {

    private static final String VIEW_REPORTS_PERMISSION = "staff.managereports";
    @Mock
    private ColorViewBuilder colorViewBuilder;
    @Mock
    private PlayerSettingsRepository playerSettingsRepository;
    @Mock
    private PlayerSettings playerSettings;
    @Captor
    private ArgumentCaptor<String> xmlCaptor;

    @Override
    public Object getGuiController() {
        return new HubGuiController(colorViewBuilder, playerSettingsRepository);
    }

    @Test
    public void hubView() throws URISyntaxException, IOException {
        when(playerSettingsRepository.get(player)).thenReturn(playerSettings);
        when(playerSettings.getGlassColor()).thenReturn(Material.BLUE_STAINED_GLASS_PANE);
        mockConfig();

        guiActionService.executeAction(player, "hub/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/hub.xml");
    }

    private void mockConfig() {
        when(templateConfigResolver.get("permissions:reports.manage.view")).thenReturn(VIEW_REPORTS_PERMISSION);
        when(permissionHandler.has(player, VIEW_REPORTS_PERMISSION)).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.name")).thenReturn("&eGUI Hub");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.reports-gui")).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.reports-title")).thenReturn("&bUnresolved reports");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.reports-lore")).thenReturn("&7Shows all open reports.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.my-reports-title")).thenReturn("&bReports assigned to you");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.my-reports-lore")).thenReturn("&7Shows reports currently assigned to you.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.assigned-reports-title")).thenReturn("&bReports In Progress");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.assigned-reports-lore")).thenReturn("&7Shows reports that are currently being worked on.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.closed-reports-title")).thenReturn("&bClosed reports");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.closed-reports-lore")).thenReturn("&7History of all closed reports.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.miner-gui")).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.miner-name")).thenReturn("&bMiner GUI");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.miner-lore")).thenReturn("&7Shows all players under the set Y value.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.protected-areas-gui")).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.protected-areas-name")).thenReturn("&bProtected Areas GUI");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.protected-areas-lore")).thenReturn("&7Shows all currently set protected areas.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.ban-gui")).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.ban-name")).thenReturn("&bBanned players GUI");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.ban-lore")).thenReturn("&7Shows all currently banned players.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.mute-gui")).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.mute-name")).thenReturn("&bMuted players GUI");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.mute-lore")).thenReturn("&7Shows all currently muted players.");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.investigation-gui")).thenReturn(true);
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.investigation-name")).thenReturn("&bInvestigations GUI");
        when(templateConfigResolver.get("staffmode-modules:modules.gui-module.investigation-lore")).thenReturn("&7Shows all investigations.");
    }
}