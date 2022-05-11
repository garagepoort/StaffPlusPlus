package net.shortninja.staffplus.core.domain.staff.location.gui;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubinggui.AsyncGui;
import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionReturnType;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.GuiParam;
import be.garagepoort.mcioc.tubinggui.model.TubingGuiActions;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocation;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationNote;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationRepository;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static be.garagepoort.mcioc.tubinggui.AsyncGui.async;
import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;

@GuiController
public class StaffLocationsGuiController {
    private static final int PAGE_SIZE = 45;
    private static final String CANCEL = "cancel";

    @ConfigProperty("%lang%:staff-locations.prefix")
    private String prefix;
    @ConfigProperty("permissions.staff-locations.delete")
    private String deletePermission;
    @ConfigProperty("permissions.staff-locations.teleport")
    private String teleportPermission;
    @ConfigProperty("permissions.staff-locations.view-notes")
    private String viewNotesPermission;
    @ConfigProperty("permissions.staff-locations.create-note")
    private String createNotePermission;
    @ConfigProperty("permissions.staff-locations.delete-note")
    private String deleteNotePermission;

    @ConfigProperty("%lang%:staff-locations.add-note-chat-info")
    private String addNoteChatInfoMessage;

    private final StaffLocationRepository staffLocationRepository;
    private final StaffLocationService staffLocationService;
    private final BukkitUtils bukkitUtils;
    private final PermissionHandler permissionHandler;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;

    public StaffLocationsGuiController(StaffLocationRepository staffLocationRepository,
                                       StaffLocationService staffLocationService,
                                       BukkitUtils bukkitUtils,
                                       PermissionHandler permissionHandler,
                                       Messages messages,
                                       OnlineSessionsManager sessionManager) {
        this.staffLocationRepository = staffLocationRepository;
        this.staffLocationService = staffLocationService;
        this.bukkitUtils = bukkitUtils;
        this.permissionHandler = permissionHandler;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    @GuiAction("staff-locations/view")
    public AsyncGui<GuiTemplate> staffLocationsOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<StaffLocation> locations = staffLocationRepository.getStaffLocations(page * PAGE_SIZE, PAGE_SIZE);

            HashMap<String, Object> params = new HashMap<>();
            params.put("locations", locations);
            return template("gui/staff-locations/overview.ftl", params);
        });
    }

    @GuiAction("staff-locations/teleport")
    public void teleport(Player player, @GuiParam("locationId") int locationId) {
        permissionHandler.validate(player, teleportPermission);
        staffLocationService.goToLocation(player, locationId);
    }

    @GuiAction("staff-locations/view/delete")
    public GuiTemplate showLocationDeletionConfirmation(Player player,
                                                        @GuiParam("locationId") int locationId,
                                                        @GuiParam("locationName") String locationName) {
        permissionHandler.validate(player, deletePermission);
        String confirmAction = GuiActionBuilder.builder()
            .action("staff-locations/delete")
            .param("locationId", String.valueOf(locationId))
            .build();

        HashMap<String, Object> params = new HashMap<>();
        params.put("confirmationMessage", "&7Delete Staff Location: &6" + locationName);
        params.put("title", "Delete staff location?");
        params.put("confirmAction", confirmAction);
        params.put("cancelAction", TubingGuiActions.BACK);
        return template("gui/commons/confirmation.ftl", params);
    }

    @GuiAction("staff-locations/delete")
    public void delete(Player player, @GuiParam("locationId") int locationId) {
        permissionHandler.validate(player, deletePermission);
        bukkitUtils.runTaskAsync(player, () -> staffLocationService.deleteLocation(player, locationId));
    }

    @GuiAction("staff-location-notes/view")
    public AsyncGui<GuiTemplate> getNotesOverview(Player player,
                                                  @GuiParam(value = "page", defaultValue = "0") int page,
                                                  @GuiParam("locationId") int locationId) {
        permissionHandler.validate(player, viewNotesPermission);
        return async(() -> {
            StaffLocation staffLocation = staffLocationService.getStaffLocation(locationId);
            List<StaffLocationNote> notes = staffLocationService.getNotesForLocation(staffLocation, page * PAGE_SIZE, PAGE_SIZE);
            HashMap<String, Object> params = new HashMap<>();
            params.put("notes", notes);
            return template("gui/staff-locations/notes-overview.ftl", params);
        });
    }

    @GuiAction("staff-location-notes/create")
    public void createNote(Player player, @GuiParam("locationId") int locationId) {
        permissionHandler.validate(player, createNotePermission);
        bukkitUtils.runTaskAsync(player, () -> {
            StaffLocation staffLocation = staffLocationService.getStaffLocation(locationId);

            messages.send(player, addNoteChatInfoMessage, prefix);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled your note", prefix);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> staffLocationService.addNote(player, staffLocation, message));
            });
        });
    }

    @GuiAction("staff-location-notes/view/delete")
    public GuiTemplate showNoteDeletionConfirmation(Player player,
                                 @GuiParam("noteId") int noteId,
                                 @GuiParam("locationId") int locationId) {
        permissionHandler.validate(player, deleteNotePermission);
        String confirmAction = GuiActionBuilder.builder()
            .action("staff-location-notes/delete")
            .param("noteId", String.valueOf(noteId))
            .param("locationId", String.valueOf(locationId))
            .build();

        HashMap<String, Object> params = new HashMap<>();
        params.put("confirmationMessage", "&7Delete note(ID=" + noteId + ")");
        params.put("title", "Delete note?");
        params.put("confirmAction", confirmAction);
        params.put("cancelAction", TubingGuiActions.BACK);
        return template("gui/commons/confirmation.ftl", params);
    }

    @GuiAction("staff-location-notes/delete")
    public AsyncGui<GuiActionReturnType> deleteNote(Player player,
                                                    @GuiParam("noteId") int noteId,
                                                    @GuiParam("locationId") int locationId) {
        permissionHandler.validate(player, deleteNotePermission);
        return async(() -> {
            StaffLocation location = staffLocationService.getStaffLocation(locationId);
            staffLocationService.deleteNote(player, location, noteId);
            return GuiActionReturnType.CLOSE;
        });
    }
}
