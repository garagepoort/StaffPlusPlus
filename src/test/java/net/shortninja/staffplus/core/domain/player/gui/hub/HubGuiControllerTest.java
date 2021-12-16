package net.shortninja.staffplus.core.domain.player.gui.hub;

import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HubGuiControllerTest extends AbstractGuiTemplateTest {

    @Captor
    private ArgumentCaptor<String> xmlCaptor;

    @Override
    public Object getGuiController() {
        return new HubGuiController();
    }

    @Test
    public void hubView() throws URISyntaxException, IOException {
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