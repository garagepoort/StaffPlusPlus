package net.shortninja.staffplus.core.reporting;

import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.domain.location.SppLocation;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.reporting.chatchannels.ReportChatChannelService;
import net.shortninja.staffplus.core.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.core.reporting.config.ReportConfiguration;
import net.shortninja.staffplus.core.reporting.gui.ManageReportsGuiController;
import net.shortninja.staffplus.core.reporting.gui.cmd.ReportFiltersMapper;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
class ReportsGuiControllerTest extends AbstractTubingGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    private final ManageReportConfiguration manageReportConfiguration = new ManageReportConfiguration();
    @Mock
    private ReportService reportService;
    @Mock
    private ManageReportService manageReportService;
    @Mock
    private BukkitUtils bukkitUtils;
    @Mock
    private Messages messages;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private ReportFiltersMapper reportFiltersMapper;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private ChatChannelService chatChannelService;
    @Mock
    private ReportChatChannelService reportChatChannelService;
    @Mock
    private World world;
    @Mock
    private ChatChannel chatChannel;
    @Mock
    private ReportConfiguration reportConfiguration;

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
        when(world.getName()).thenReturn("world");
        doReturn(true).when(templateConfigResolverSpy).get("server-sync-module.report-sync");
        when(permissionHandler.has(player, "staff.chatchannels.join.report")).thenReturn(true);
    }

    @Override
    public Object getGuiController() {
        return new ManageReportsGuiController(
            permissionHandler,
            manageReportConfiguration,
            reportService,
            bukkitUtils,
            messages,
            manageReportService,
            onlineSessionsManager,
            reportFiltersMapper,
            playerManager,
            chatChannelService,
            reportChatChannelService,
            reportConfiguration);
    }

    @Test
    public void viewReportDetail() throws URISyntaxException, IOException {
        when(reportService.getReport(12)).thenReturn(buildReport());
        when(chatChannelService.findChannel(String.valueOf(12), ChatChannelType.REPORT)).thenReturn(Optional.of(chatChannel));

        validateSnapshot(player, "manage-reports/view/detail?reportId=12", "/guitemplates/report/report-detail.xml");
    }

    private Report buildReport() {
        Report report = new Report(
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377"),
            "culpritName",
            12,
            "reason",
            "reporterName",
            UUID.fromString("450e2eba-5fe0-4938-a820-f1c3680327de"),
            1234567L,
            ReportStatus.IN_PROGRESS,
            "staffName",
            UUID.fromString("b1a8910f-476d-4f93-a327-d8134fd26f5c"),
            null,
            "server",
            new Location(world, 4, 5, 6),
            new SppLocation(789, "world", 4, 5, 6, "server"),
            "type"
        );
        report.setTimestamp(ZonedDateTime.of(2022, 2, 25, 14, 30, 0, 0, ZoneOffset.UTC));
        return report;
    }
}