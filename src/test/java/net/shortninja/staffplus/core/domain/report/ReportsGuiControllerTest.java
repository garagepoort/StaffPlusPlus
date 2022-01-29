package net.shortninja.staffplus.core.domain.report;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.SppLocation;
import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.ReportsGuiController;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.cmd.ReportFiltersMapper;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.Location;
import org.bukkit.World;
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
class ReportsGuiControllerTest extends AbstractGuiTemplateTest {

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
    private Options options;
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
    private World world;
    @Mock
    private ChatChannel chatChannel;

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
        when(world.getName()).thenReturn("world");
        doReturn(true).when(templateConfigResolverSpy).get("server-sync-module.report-sync");
        when(permissionHandler.has(player, "staff.chatchannels.join.report")).thenReturn(true);
    }

    @Override
    public Object getGuiController() {
        return new ReportsGuiController(
            permissionHandler,
            manageReportConfiguration,
            reportService,
            options,
            bukkitUtils,
            messages,
            manageReportService,
            onlineSessionsManager,
            reportFiltersMapper,
            playerManager,
            chatChannelService
        );
    }

    @Test
    public void viewReportDetail() throws URISyntaxException, IOException {
        when(reportService.getReport(12)).thenReturn(buildReport());
        when(chatChannelService.findChannel(String.valueOf(12), ChatChannelType.REPORT)).thenReturn(Optional.of(chatChannel));

        guiActionService.executeAction(player, "manage-reports/view/detail?reportId=12&backAction=back");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/report/report-detail.xml");
    }

    private Report buildReport() {
        return new Report(
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
            new SppLocation("world",4,5,6, "server"),
            "type"
        );
    }
}