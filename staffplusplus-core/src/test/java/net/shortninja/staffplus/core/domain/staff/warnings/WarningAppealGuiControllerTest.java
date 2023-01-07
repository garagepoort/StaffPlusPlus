package net.shortninja.staffplus.core.domain.staff.warnings;

import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractTubingGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.WarningAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.WarningAppealGuiController;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
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
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WarningAppealGuiControllerTest extends AbstractTubingGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    private WarningAppealConfiguration warningAppealConfiguration;
    @Mock
    private AppealService appealService;
    @Mock
    private WarnService warnService;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private Options options;
    @Mock
    private Messages messages;
    @Mock
    private BukkitUtils bukkitUtils;
    @Mock
    private ActionService actionService;
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

    @BeforeEach
    public void setUp() {
        super.setUp();
        when(permissionHandler.has(eq(player), anyString())).thenReturn(true);
    }

    @Override
    public Object getGuiController() {
        warningAppealConfiguration = new WarningAppealConfiguration();
        return new WarningAppealGuiController(
            appealService,
            warnService,
            messages,
            onlineSessionsManager,
            options,
            bukkitUtils,
            warningAppealConfiguration,
            actionService,
            permissionHandler,
            playerManager);
    }

    @Test
    public void appealReasonSelect() throws URISyntaxException, IOException {
        warningAppealConfiguration.appealReasons = Arrays.asList("Reason 1", "Reason 2", "Reason 3");

        guiActionService.executeAction(player, "manage-warning-appeals/view/create/reason-select?warningId=12");

        verify(tubingGuiXmlParser).toTubingGui(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/appeal-reason-select.xml");
    }

    @Test
    public void appealedWarningsOverview() throws URISyntaxException, IOException {
        when(warnService.getAppealedWarnings(0, 45)).thenReturn(Collections.singletonList(buildWarning()));

        guiActionService.executeAction(player, "manage-warnings/view/appealed-warnings");

        verify(tubingGuiXmlParser).toTubingGui(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/warnings/appealed-warnings.xml");
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