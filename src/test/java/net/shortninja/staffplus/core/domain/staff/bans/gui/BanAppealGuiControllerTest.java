package net.shortninja.staffplus.core.domain.staff.bans.gui;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.ban.appeals.BanAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.appeals.BanAppealGuiController;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
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
class BanAppealGuiControllerTest extends AbstractGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    @Mock
    private BanService banService;
    @Mock
    private Messages messages;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private AppealService appealService;
    @Mock
    private BukkitUtils bukkitUtils;

    @Captor
    private ArgumentCaptor<String> xmlCaptor;
    private static MockedStatic<GuiUtils> guiUtilsMockedStatic;
    private BanAppealConfiguration banAppealConfiguration;

    @BeforeAll
    public static void beforeAll() {
        guiUtilsMockedStatic = mockStatic(GuiUtils.class);
        guiUtilsMockedStatic.when(() -> GuiUtils.parseTimestamp(CREATION_DATE, TIMESTAMP_FORMAT)).thenReturn("01/09/2021-01:11:15");
        guiUtilsMockedStatic.when(() -> GuiUtils.parseTimestampSeconds(CREATION_DATE, TIMESTAMP_FORMAT)).thenReturn("01/09/2021-01:11:15");
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
        when(permissionHandler.has(eq(player), anyString())).thenReturn(true);
        doReturn(true).when(templateConfigResolverSpy).get("server-sync-module.ban-sync");
    }

    @Override
    public Object getGuiController() {
        banAppealConfiguration = new BanAppealConfiguration();
        return new BanAppealGuiController(
            appealService,
            banService,
            messages,
            onlineSessionsManager,
            bukkitUtils,
            banAppealConfiguration,
            permissionHandler,
            playerManager);
    }

    @Test
    public void appealDetail() throws URISyntaxException, IOException {
        when(appealService.getAppeal(1)).thenReturn(buildAppeal());

        guiActionService.executeAction(player, "manage-ban-appeals/view/detail?appealId=1&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/bans/appeal-detail.xml");
    }

    @Test
    public void appealReasonSelect() throws URISyntaxException, IOException {
        banAppealConfiguration.appealReasons = Arrays.asList("Reason 1", "Reason 2", "Reason 3");

        guiActionService.executeAction(player, "manage-ban-appeals/view/create/reason-select?banId=12&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/bans/appeal-reason-select.xml");
    }

    @Test
    public void appealedBansOverview() throws URISyntaxException, IOException {
        when(banService.getAppealedBans(0, 45)).thenReturn(Collections.singletonList(buildBan()));

        guiActionService.executeAction(player, "manage-bans/view/appealed-bans");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/bans/appealed-bans.xml");
    }

    private Appeal buildAppeal() {
        return new Appeal(
            1,
            12,
            UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"),
            "appealer",
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377"),
            "resolver",
            "reason",
            null,
            AppealStatus.OPEN,
            CREATION_DATE,
            AppealableType.BAN
        );
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
            null,
            null
        );
    }
}