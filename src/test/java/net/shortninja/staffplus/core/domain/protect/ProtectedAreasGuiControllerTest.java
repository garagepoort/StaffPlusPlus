package net.shortninja.staffplus.core.domain.protect;

import net.shortninja.staffplus.core.common.gui.AbstractTubingGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import net.shortninja.staffplus.core.domain.staff.protect.config.ProtectConfiguration;
import net.shortninja.staffplus.core.domain.staff.protect.gui.ProtectedAreasGuiController;
import net.shortninja.staffplus.core.domain.staff.teleport.TeleportService;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProtectedAreasGuiControllerTest extends AbstractTubingGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    private final ProtectConfiguration protectConfiguration = new ProtectConfiguration();
    @Mock
    private ProtectService protectService;
    @Mock
    private TeleportService teleportService;
    @Mock
    private BukkitUtils bukkitUtils;
    private static World world;

    private static MockedStatic<GuiUtils> guiUtilsMockedStatic;
    private static Location cornerPoint1;
    private static Location cornerPoint2;
    ;

    @BeforeAll
    public static void beforeAll() {
        world = mock(World.class);
        cornerPoint1 = new Location(world, 1, 2, 3);
        cornerPoint2 = new Location(world, 4, 5, 6);
        guiUtilsMockedStatic = mockStatic(GuiUtils.class);
        guiUtilsMockedStatic.when(() -> GuiUtils.parseTimestamp(CREATION_DATE, TIMESTAMP_FORMAT)).thenReturn("01/09/2021-01:11:15");
        guiUtilsMockedStatic.when(() -> GuiUtils.getNextPage(anyString(), anyInt())).thenReturn("goNext");
        guiUtilsMockedStatic.when(() -> GuiUtils.getPreviousPage(anyString(), anyInt())).thenReturn("goPrevious");
        guiUtilsMockedStatic.when(() -> GuiUtils.parseLocation(cornerPoint1)).thenReturn("X: 1 Y: 2 Z: 3");
        guiUtilsMockedStatic.when(() -> GuiUtils.parseLocation(cornerPoint2)).thenReturn("X: 4 Y: 5 Z: 6");
    }

    @AfterAll
    public static void close() {
        guiUtilsMockedStatic.close();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        when(world.getName()).thenReturn("world");
        doReturn(true).when(templateConfigResolverSpy).get("server-sync-module.ban-sync");
        protectConfiguration.modeGuiProtectedAreasTitle = "Protected areas";
    }

    @Override
    public Object getGuiController() {
        return new ProtectedAreasGuiController(
            protectService,
            teleportService,
            bukkitUtils,
            protectConfiguration);
    }

    @Test
    public void viewAreasOverview() throws URISyntaxException, IOException {
        when(protectService.getAllProtectedAreasPaginated(0, 45)).thenReturn(Collections.singletonList(buildArea()));

        validateSnapshot(player, "protected-areas/view", "/guitemplates/protect/areas-overview.xml");
    }

    @Test
    public void viewAreasDetail() throws URISyntaxException, IOException {
        when(protectService.getById(12)).thenReturn(buildArea());

        validateSnapshot(player, "protected-areas/view/detail?areaId=12", "/guitemplates/protect/area-detail.xml");
    }

    private ProtectedArea buildArea() {
        return new ProtectedArea(12,
            "area",
            cornerPoint1,
            cornerPoint2,
            UUID.fromString("8fc39a71-63ba-4a4b-99e8-66f5791dd377")
        );
    }
}