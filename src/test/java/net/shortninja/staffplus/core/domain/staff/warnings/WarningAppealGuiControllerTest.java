package net.shortninja.staffplus.core.domain.staff.warnings;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.ManageAppealViewBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.WarningAppealGuiController;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WarningAppealGuiControllerTest extends AbstractGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    private AppealConfiguration appealConfiguration;
    @Mock
    private AppealService appealService;
    @Mock
    private WarnService warnService;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private Options options;
    @Mock
    private ManageAppealViewBuilder manageAppealViewBuilder;
    @Mock
    private Messages messages;
    @Mock
    private BukkitUtils bukkitUtils;

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
        when(templateConfigResolver.get("warnings-module.appeals.enabled")).thenReturn(true);
        when(templateConfigResolver.get("warnings-module.appeals.fixed-reason")).thenReturn(false);
        when(templateConfigResolver.get("server-sync-module.warning-sync")).thenReturn(true);
        when(templateConfigResolver.get("investigations-module.enabled")).thenReturn(true);
        when(templateConfigResolver.get("permissions:warnings.manage.delete")).thenReturn("warning.delete");
        when(templateConfigResolver.get("permissions:warnings.manage.expire")).thenReturn("warning.expire");
        when(templateConfigResolver.get("permissions:warnings.appeals.create-others")).thenReturn("warning.appeals.create-other");
        when(templateConfigResolver.get("permissions:warnings.appeals.approve")).thenReturn("warning.appeals.approve");
        when(templateConfigResolver.get("permissions:warnings.appeals.reject")).thenReturn("warning.appeals.reject");
        when(templateConfigResolver.get("permissions:warnings.appeals.fixed-reason")).thenReturn("warning.appeals.fixed-reason");
        when(templateConfigResolver.get("timestamp-format")).thenReturn(TIMESTAMP_FORMAT);
        when(permissionHandler.has(eq(player), anyString())).thenReturn(true);
    }

    @Override
    public Object getGuiController() {
        appealConfiguration = new AppealConfiguration();
        return new WarningAppealGuiController(manageAppealViewBuilder,
            appealService,
            warnService,
            messages,
            onlineSessionsManager,
            options,
            bukkitUtils,
            appealConfiguration);
    }

    @Test
    public void appealReasonSelect() throws URISyntaxException, IOException {
        appealConfiguration.appealReasons = Arrays.asList("Reason 1", "Reason 2", "Reason 3");

        guiActionService.executeAction(player, "manage-warning-appeals/view/create/reason-select?warningId=12&backAction=goBack/view");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/appeal-reason-select.xml");
    }

}