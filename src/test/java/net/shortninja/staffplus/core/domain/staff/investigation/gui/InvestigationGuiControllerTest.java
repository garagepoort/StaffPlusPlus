package net.shortninja.staffplus.core.domain.staff.investigation.gui;

import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation.InvestigationGuiController;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InvestigationGuiControllerTest extends AbstractGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    @Mock
    private InvestigationService investigationService;
    @Mock
    private BukkitUtils bukkitUtils;
    @Mock
    private PlayerManager playerManager;

    @Captor
    private ArgumentCaptor<String> xmlCaptor;
    private static MockedStatic<GuiUtils> guiUtilsMockedStatic;

    @BeforeAll
    public static void beforeAll() {
        guiUtilsMockedStatic = mockStatic(GuiUtils.class);
        guiUtilsMockedStatic.when(() -> GuiUtils.parseTimestamp(CREATION_DATE, TIMESTAMP_FORMAT)).thenReturn("01/09/2021-01:11:15");
        guiUtilsMockedStatic.when(() -> GuiUtils.getNextPage(anyString(), anyInt())).thenReturn("goNext");
        guiUtilsMockedStatic.when(() -> GuiUtils.getPreviousPage(anyString(), anyInt())).thenReturn("goPrevious");
    }

    @AfterAll
    public static void close() {
        guiUtilsMockedStatic.close();
    }

    @Override
    public Object getGuiController() {
        return new InvestigationGuiController(
            playerManager,
            investigationService,
            bukkitUtils);
    }

    @Test
    public void viewOverview() throws URISyntaxException, IOException {
        when(investigationService.getAllInvestigations(0, 45)).thenReturn(Collections.singletonList(buildInvestigation()));

        guiActionService.executeAction(player, "manage-investigations/view/overview");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/investigations/investigations-overview.xml");
    }

    @Test
    public void viewInvestigationDetail() throws URISyntaxException, IOException {
        when(investigationService.getInvestigation(12)).thenReturn(buildInvestigation());

        guiActionService.executeAction(player, "manage-investigations/view/detail?investigationId=12&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/investigations/investigation-detail.xml");
    }

    private Investigation buildInvestigation() {
        return new Investigation(12,
            CREATION_DATE,
            null,
            "investigator",
            UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"),
            "investigated",
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377"),
            "ServerName",
            InvestigationStatus.OPEN
        );
    }
}