package net.shortninja.staffplus.core.domain.staff.mute.gui;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.database.MuteRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
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

import static java.util.Optional.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MuteGuiControllerTest extends AbstractGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    @Mock
    private Messages messages;
    @Mock
    private MuteService muteService;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private BukkitUtils bukkitUtils;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private MuteRepository muteRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SppPlayer sppPlayer;

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
        doReturn(true).when(templateConfigResolverSpy).get("server-sync-module.mute-sync");
    }

    @Override
    public Object getGuiController() {
        return new MuteGuiController(messages,
            muteService,
            onlineSessionsManager,
            bukkitUtils,
            playerManager,
            muteRepository);
    }

    @Test
    public void viewAllActiveMutes() throws URISyntaxException, IOException {
        when(muteService.getAllPaged(0, 45)).thenReturn(Collections.singletonList(buildMute()));

        guiActionService.executeAction(player, "manage-mutes/view/all-active");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/mutes/active-mutes.xml");
    }

    @Test
    public void viewPlayerMuteHistory() throws URISyntaxException, IOException {
        UUID uuid = UUID.fromString("65107a8b-500e-4cf4-9b13-87f838606b08");
        when(playerManager.getOnOrOfflinePlayer("targetName")).thenReturn(of(sppPlayer));
        when(sppPlayer.getUsername()).thenReturn("targetName");
        when(sppPlayer.getId()).thenReturn(uuid);
        when(muteRepository.getMutesForPlayerPaged(uuid, 0, 45)).thenReturn(Collections.singletonList(buildMute()));

        guiActionService.executeAction(player, "manage-mutes/view/history?targetPlayerName=targetName");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/mutes/history-player-mutes.xml");
    }

    @Test
    public void viewMuteDetail() throws URISyntaxException, IOException {
        doReturn(true).when(templateConfigResolverSpy).get("investigations-module.enabled");
        when(muteService.getById(12)).thenReturn(buildMute());

        guiActionService.executeAction(player, "manage-mutes/view/detail?muteId=12");

        verify(tubingGuiXmlParser).parseHtml(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/mutes/mute-detail.xml");
    }

    @NotNull
    private Mute buildMute() {
        return new Mute(12,
            "Mute reason",
            CREATION_DATE,
            null,
            "player",
            UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"),
            "issuer",
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377"),
            null,
            null,
            null,
            "ServerName",
            false
        );
    }
}