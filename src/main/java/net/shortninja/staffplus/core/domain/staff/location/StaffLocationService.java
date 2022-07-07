package net.shortninja.staffplus.core.domain.staff.location;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.bungee.ServerSwitcher;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.location.SppLocation;
import net.shortninja.staffplusplus.stafflocations.StaffLocationCreatedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationDeletedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationFilters;
import net.shortninja.staffplusplus.stafflocations.StaffLocationIconChangedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationLocationChangedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationNameChangedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationNoteCreatedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationNoteDeletedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationTeleportedEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest.CreateStoredCommandRequestBuilder.commandBuilder;

@IocBean
public class StaffLocationService {

    private final StaffLocationRepository staffLocationRepository;
    private final StaffLocationNotesRepository staffLocationNotesRepository;
    private final Options options;
    private final ActionService actionService;

    @ConfigProperty("commands:staff-locations.teleport-to-stafflocation")
    public List<String> teleportCommands;

    public StaffLocationService(StaffLocationRepository locationRepository, StaffLocationNotesRepository staffLocationNotesRepository, Options options, ActionService actionService) {
        this.staffLocationRepository = locationRepository;
        this.staffLocationNotesRepository = staffLocationNotesRepository;
        this.options = options;
        this.actionService = actionService;
    }

    public StaffLocation saveLocation(Player player, String name, Material icon) {
        StaffLocation staffLocation = new StaffLocation(name, player, new SppLocation(player.getLocation(), options.serverName), icon);
        int id = staffLocationRepository.saveStaffLocation(player, staffLocation);
        staffLocation.setId(id);

        sendEvent(new StaffLocationCreatedEvent(staffLocation));
        return staffLocation;
    }

    public void deleteLocation(Player player, int locationId) {
        StaffLocation staffLocation = getStaffLocation(locationId);
        staffLocationRepository.delete(locationId);

        sendEvent(new StaffLocationDeletedEvent(staffLocation, player));
    }

    public StaffLocation getStaffLocation(int locationId) {
        return staffLocationRepository.getById(locationId).orElseThrow(() -> new BusinessException("No location found for ID: [" + locationId + "]"));
    }

    public void addNote(Player noteTaker, StaffLocation staffLocation, String note) {
        if (StringUtils.isBlank(note)) {
            throw new BusinessException("Staff location note cannot be empty");
        }

        StaffLocationNote staffLocationNote = new StaffLocationNote(
            staffLocation.getId(),
            note,
            noteTaker);

        staffLocationNotesRepository.addNote(staffLocationNote);
        sendEvent(new StaffLocationNoteCreatedEvent(staffLocation, staffLocationNote));
    }

    public List<StaffLocationNote> getNotesForLocation(StaffLocation staffLocation, int offset, int amount) {
        return staffLocationNotesRepository.getAllNotes(staffLocation.getId(), offset, amount);
    }

    public void goToLocation(Player player, int locationId) {
        StaffLocation staffLocation = getStaffLocation(locationId);
        Location location = staffLocation.getLocation();

        if (staffLocation.getServerName().equalsIgnoreCase(options.serverName)) {
            player.teleport(location);
        } else {
            teleportToOtherServer(player, locationId, staffLocation);
        }
        sendEvent(new StaffLocationTeleportedEvent(staffLocation, player));
    }

    private void teleportToOtherServer(Player player, int locationId, StaffLocation staffLocation) {
        if (teleportCommands.isEmpty()) {
            throw new BusinessException("No teleport commands configured for staff locations.");
        }

        actionService.createCommand(
            commandBuilder()
                .command("staffplusplus:" + teleportCommands.get(0) + " " + locationId)
                .executor(player.getUniqueId())
                .executorRunStrategy(ActionRunStrategy.DELAY)
                .serverName(staffLocation.getServerName())
                .build());
        ServerSwitcher.switchServer(player, staffLocation.getServerName());
    }

    public void deleteNote(Player player, StaffLocation location, int noteId) {
        Optional<StaffLocationNote> noteEntity = staffLocationNotesRepository.find(noteId);
        if (noteEntity.isPresent()) {
            staffLocationNotesRepository.removeNote(noteId);
            sendEvent(new StaffLocationNoteDeletedEvent(player, location, noteEntity.get()));
        }
    }

    public List<StaffLocation> findLocations(StaffLocationFilters staffLocationFilters, int offset, int amount) {
        return staffLocationRepository.findLocations(staffLocationFilters, offset, amount);
    }

    public void updateName(Player player, int staffLocationId, String name) {
        StaffLocation staffLocation = getStaffLocation(staffLocationId);
        staffLocation.setName(name);
        staffLocationRepository.updateStaffLocation(staffLocation);
        sendEvent(new StaffLocationNameChangedEvent(player, staffLocation));
    }

    public void updateIcon(Player player, int staffLocationId, Material icon) {
        StaffLocation staffLocation = getStaffLocation(staffLocationId);
        staffLocation.setIcon(icon);
        staffLocationRepository.updateStaffLocation(staffLocation);
        sendEvent(new StaffLocationIconChangedEvent(player, staffLocation));
    }

    public void updateLocation(Player player, int locationId) {
        StaffLocation staffLocation = getStaffLocation(locationId);
        SppLocation sppLocation = staffLocation.getSppLocation();
        sppLocation.setWorldName(player.getLocation().getWorld().getName());
        sppLocation.setServerName(options.serverName);
        sppLocation.setX(player.getLocation().getX());
        sppLocation.setY(player.getLocation().getY());
        sppLocation.setZ(player.getLocation().getZ());

        staffLocationRepository.updateStaffLocation(staffLocation);
        sendEvent(new StaffLocationLocationChangedEvent(player, staffLocation));
    }
}
