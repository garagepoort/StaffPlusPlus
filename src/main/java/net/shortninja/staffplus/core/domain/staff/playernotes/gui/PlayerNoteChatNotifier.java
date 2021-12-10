package net.shortninja.staffplus.core.domain.staff.playernotes.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplusplus.playernotes.IPlayerNote;
import net.shortninja.staffplusplus.playernotes.PlayerNoteCreatedEvent;
import net.shortninja.staffplusplus.playernotes.PlayerNoteDeletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@IocBean
@IocListener
public class PlayerNoteChatNotifier implements Listener {

    @ConfigProperty("permissions:player-notes.notifications")
    private String permissionNoteNotifications;

    private final Messages messages;

    public PlayerNoteChatNotifier(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void onNoteCreated(PlayerNoteCreatedEvent event) {
        event.getSender().getCommandSender().ifPresent(s -> {
            messages.send(s, replaceNotePlaceholders(event.getPlayerNote(), messages.noteAdded), messages.prefixGeneral);
        });

        if (!event.getPlayerNote().isPrivateNote()) {
            messages.sendGroupMessage(replaceNotePlaceholders(event.getPlayerNote(), messages.noteCreatedNotification), permissionNoteNotifications, messages.prefixPlayerNotes);
        }
    }

    @EventHandler
    public void onNoteDeleted(PlayerNoteDeletedEvent event) {
        if (event.getSender().isOnline()) {
            messages.send(event.getSender().getPlayer(),
                replaceNotePlaceholders(event.getPlayerNote(), messages.noteDeleted),
                messages.prefixGeneral);
        }
    }

    @NotNull
    private String replaceNotePlaceholders(IPlayerNote playerNote, String noteMessage) {
        return noteMessage
            .replace("%target%", playerNote.getTargetName())
            .replace("%notedBy%", playerNote.getNotedByName())
            .replace("%noteId%", playerNote.getId().toString())
            .replace("%note%", playerNote.getNote());
    }
}
