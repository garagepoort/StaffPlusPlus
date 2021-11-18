package net.shortninja.staffplus.core.domain.staff.bans.gui;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.database.BansRepository;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.BanGuiController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
class BanGuiControllerTest extends AbstractGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    @Mock
    private BanService banService;
    @Mock
    private BansRepository bansRepository;
    @Mock
    private Messages messages;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;

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

    @BeforeEach
    public void setUp() {
        super.setUp();
        when(templateConfigResolver.get("server-sync-module.ban-sync")).thenReturn(true);
        when(templateConfigResolver.get("timestamp-format")).thenReturn(TIMESTAMP_FORMAT);
    }

    @Override
    public Object getGuiController() {
        return new BanGuiController(
            messages,
            banService,
            bansRepository,
            onlineSessionsManager,
            playerManager);
    }

    @Test
    public void viewActiveBansOverview() throws URISyntaxException, IOException {
        when(banService.getAllPaged(0, 45)).thenReturn(Collections.singletonList(buildBan()));

        guiActionService.executeAction(player, "manage-bans/view/overview");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/bans/bans-overview.xml");
    }

    @Test
    public void viewInvestigationDetail() throws URISyntaxException, IOException {
        when(banService.getById(12)).thenReturn(buildBan());

        guiActionService.executeAction(player, "manage-bans/view/detail?banId=12&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/bans/ban-detail.xml");
    }

    private Ban buildBan() {
        return new Ban(12,
            "reason",
            CREATION_DATE,
            null,
            "target",
            UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"),
            "issuer",
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377"),
            null,
            null,
            null,
            "ServerName",
            false,
            false,
            null
        );
    }
}