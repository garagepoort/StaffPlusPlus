package net.shortninja.staffplus.core.domain.staff.location.gui;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.stafflocations.StaffLocationCreatedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationDeletedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationIconChangedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationLocationChangedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationNameChangedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationNoteCreatedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationNoteDeletedEvent;
import net.shortninja.staffplusplus.stafflocations.StaffLocationTeleportedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener
public class StaffLocationsChatNotifier implements Listener {

    private final PlayerManager playerManager;
    private final Messages messages;

    @ConfigProperty("%lang%:staff-locations.prefix")
    private String prefix;
    @ConfigProperty("%lang%:staff-locations.created")
    private String createdMessage;
    @ConfigProperty("%lang%:staff-locations.updated")
    private String updatedMessage;
    @ConfigProperty("%lang%:staff-locations.deleted")
    private String deletedMessage;
    @ConfigProperty("%lang%:staff-locations.teleported")
    private String teleportedMessage;
    @ConfigProperty("%lang%:staff-locations.note-created")
    private String noteCreatedMessage;
    @ConfigProperty("%lang%:staff-locations.note-deleted")
    private String noteDeletedMessage;

    public StaffLocationsChatNotifier(PlayerManager playerManager, Messages messages) {
        this.playerManager = playerManager;
        this.messages = messages;
    }

    @EventHandler
    public void staffLocationCreated(StaffLocationCreatedEvent event) {
        playerManager.getOnlinePlayer(event.getStaffLocation().getCreatorUuid()).ifPresent(p -> messages.send(p, createdMessage, prefix));
    }

    @EventHandler
    public void staffLocationNameChanged(StaffLocationNameChangedEvent event) {
        messages.send(event.getEditedBy(), updatedMessage, prefix);
    }

    @EventHandler
    public void staffLocationIconChanged(StaffLocationIconChangedEvent event) {
        messages.send(event.getEditedBy(), updatedMessage, prefix);
    }

    @EventHandler
    public void staffLocationLocationChanged(StaffLocationLocationChangedEvent event) {
        messages.send(event.getEditedBy(), updatedMessage, prefix);
    }

    @EventHandler
    public void staffLocationDeleted(StaffLocationDeletedEvent event) {
        messages.send(event.getDeletedByPlayer(), deletedMessage, prefix);
    }

    @EventHandler
    public void staffLocationTeleported(StaffLocationTeleportedEvent event) {
        messages.send(event.getTeleportedPlayer(), teleportedMessage.replace("%name%", event.getStaffLocation().getName()), prefix);
    }

    @EventHandler
    public void staffLocationNoteCreated(StaffLocationNoteCreatedEvent event) {
        playerManager.getOnlinePlayer(event.getStaffLocationNote().getNotedByUuid()).ifPresent(p -> messages.send(p, noteCreatedMessage, prefix));
    }

    @EventHandler
    public void staffLocationNoteDeleted(StaffLocationNoteDeletedEvent event) {
        messages.send(event.getDeletedBy(), noteDeletedMessage, prefix);
    }
}
