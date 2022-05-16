package net.shortninja.staffplus.core.domain.staff.location.gui;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.gui.AbstractTubingGuiTemplateTest;
import net.shortninja.staffplus.core.common.gui.GuiUtils;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocation;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationNote;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationRepository;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationService;
import org.bukkit.Location;
import org.bukkit.Material;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StaffLocationGuiControllerTest extends AbstractTubingGuiTemplateTest {

    private static final long CREATION_DATE = 1630537429182L;
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    @Mock
    private StaffLocationService staffLocationService;
    @Mock
    private StaffLocationRepository staffLocationRepository;
    @Mock
    private BukkitUtils bukkitUtils;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private Messages messages;
    @Mock
    private OnlineSessionsManager onlineSessionsManager;
    @Mock
    private Location location;

    @Captor
    private ArgumentCaptor<String> xmlCaptor;
    private static MockedStatic<GuiUtils> guiUtilsMockedStatic;

    @BeforeAll
    public static void beforeAll() {
        guiUtilsMockedStatic = mockStatic(GuiUtils.class);
        guiUtilsMockedStatic.when(() -> GuiUtils.parseTimestamp(CREATION_DATE, TIMESTAMP_FORMAT)).thenReturn("01/09/2021-01:11:15");
        guiUtilsMockedStatic.when(() -> GuiUtils.getNextPage(anyString(), anyInt())).thenReturn("goNext");
        guiUtilsMockedStatic.when(() -> GuiUtils.getPreviousPage(anyString(), anyInt())).thenReturn("goPrevious");
        guiUtilsMockedStatic.when(() -> GuiUtils.parseLocation(any())).thenReturn("world | 1 2 3");
    }

    @AfterAll
    public static void close() {
        guiUtilsMockedStatic.close();
    }

    @Override
    public Object getGuiController() {
        return new StaffLocationsGuiController(
            staffLocationRepository,
            staffLocationService,
            bukkitUtils,
            permissionHandler,
            messages,
            onlineSessionsManager,
            new StaffLocationFiltersMapper(), guiActionService);
    }

    @Test
    public void viewOverview() throws URISyntaxException, IOException {
        when(permissionHandler.has(eq(player), eq("staff.staff-locations.notes.view"))).thenReturn(true);
        when(permissionHandler.has(eq(player), eq("staff.staff-locations.delete"))).thenReturn(true);
        when(permissionHandler.has(eq(player), eq("staff.staff-locations.teleport"))).thenReturn(true);
        when(staffLocationRepository.getStaffLocations(0, 45)).thenReturn(Collections.singletonList(buildStaffLocation()));

        guiActionService.executeAction(player, "staff-locations/view");

        verify(tubingGuiXmlParser).toTubingGui(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/stafflocations/stafflocations-overview.xml");
    }

    @Test
    public void viewOverviewWithoutPermissions() throws URISyntaxException, IOException {
        when(permissionHandler.has(eq(player), eq("staff.staff-locations.notes.view"))).thenReturn(false);
        when(permissionHandler.has(eq(player), eq("staff.staff-locations.delete"))).thenReturn(false);
        when(permissionHandler.has(eq(player), eq("staff.staff-locations.teleport"))).thenReturn(false);
        when(staffLocationRepository.getStaffLocations(0, 45)).thenReturn(Collections.singletonList(buildStaffLocation()));

        guiActionService.executeAction(player, "staff-locations/view");

        verify(tubingGuiXmlParser).toTubingGui(eq(player), xmlCaptor.capture());
        validateMaterials(xmlCaptor.getValue());
        validateXml(xmlCaptor.getValue(), "/guitemplates/stafflocations/stafflocations-overview-no-permission.xml");
    }

    private StaffLocation buildStaffLocation() {
        return new StaffLocation(12,
            "stafflocation",
            "creatorName",
            UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"),
            location,
            "serverName",
            CREATION_DATE,
            new StaffLocationNote(13, 12, "My note", UUID.fromString("d38f72ea-551a-4a65-8401-d83465a7f596"), "noteCreatorName", CREATION_DATE),
            Material.PAPER);
    }
}