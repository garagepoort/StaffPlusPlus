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
        when(permissionHandler.has(player, "staff.reports.manage.view")).thenReturn(true);
    }
}