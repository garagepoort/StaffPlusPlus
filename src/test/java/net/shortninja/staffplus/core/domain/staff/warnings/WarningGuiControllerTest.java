package net.shortninja.staffplus.core.domain.staff.warnings;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.WarningsGuiController;
import net.shortninja.staffplusplus.session.SppPlayer;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WarningGuiControllerTest extends AbstractGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    @Mock
    private WarnService warnService;
    @Mock
    private BukkitUtils bukkitUtils;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private ActionService actionService;
    @Mock
    private WarningConfiguration warningConfiguration;
    @Mock
    private Messages messages;
    @Mock
    private SppPlayer targetPlayer;

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
        doReturn(true).when(templateConfigResolverSpy).get("server-sync-module.warning-sync");
        when(permissionHandler.has(eq(player), anyString())).thenReturn(true);
    }

    @Override
    public Object getGuiController() {
        return new WarningsGuiController(
            playerManager,
            warnService,
            onlineSessionsManager,
            warningConfiguration,
            messages,
            bukkitUtils,
            actionService);
    }

    @Test
    public void myWarningsOverview() throws URISyntaxException, IOException {
        when(warnService.getWarnings(PLAYER_UUID, 0, 45, false)).thenReturn(Collections.singletonList(buildWarning()));

        guiActionService.executeAction(player, "manage-warnings/view/my-warnings");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/my-warnings-overview.xml");
    }

    @Test
    public void warningsOverview() throws URISyntaxException, IOException {
        when(warnService.getAllWarnings(0, 45, true)).thenReturn(Collections.singletonList(buildWarning()));

        guiActionService.executeAction(player, "manage-warnings/view/overview");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/warnings-overview.xml");
    }

    @Test
    public void warningDetail() throws URISyntaxException, IOException {
        when(warnService.getWarning(12)).thenReturn(buildWarning());

        guiActionService.executeAction(player, "manage-warnings/view/detail?warningId=12&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/warning-detail.xml");
    }

    @Test
    public void severitySelect() throws URISyntaxException, IOException {
        when(playerManager.getOnOrOfflinePlayer("player2")).thenReturn(Optional.of(targetPlayer));
        when(targetPlayer.getUsername()).thenReturn("player2");
        when(warningConfiguration.getSeverityLevels()).thenReturn(Arrays.asList(
            new WarningSeverityConfiguration("MINOR", 1, 2000, "minor reason", true),
            new WarningSeverityConfiguration("MAJOR", 2, 2000, "major reason", true)));

        guiActionService.executeAction(player, "manage-warnings/view/select-severity?targetPlayerName=player2&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/severity-select.xml");
    }

    private Warning buildWarning() {
        return new Warning(
            UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"),
            "target",
            12,
            "reason",
            "issuer",
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377"),
            CREATION_DATE,
            1,
            "MINOR",
            false,
            "ServerName",
            null,
            false
        );
    }
}