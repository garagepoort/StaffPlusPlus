package net.shortninja.staffplus.core.domain.staff.playernotes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.playernotes.database.PlayerNoteRepository;
import net.shortninja.staffplusplus.playernotes.PlayerNoteCreatedEvent;
import net.shortninja.staffplusplus.playernotes.PlayerNoteDeletedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class PlayerNoteService {

    @ConfigProperty("permissions:player-notes.create")
    private String permissionCreateNote;
    @ConfigProperty("permissions:player-notes.create-private")
    private String permissionCreatePrivateNote;
    @ConfigProperty("permissions:player-notes.delete")
    private String permissionDelete;
    @ConfigProperty("permissions:player-notes.delete-other")
    private String permissionDeleteOther;

    private final PlayerNoteRepository playerNoteRepository;
    private final PermissionHandler permissionHandler;
    private final Options options;

    public PlayerNoteService(PlayerNoteRepository playerNoteRepository, PermissionHandler permissionHandler, Options options) {
        this.playerNoteRepository = playerNoteRepository;
        this.permissionHandler = permissionHandler;
        this.options = options;
    }

    public void createNote(CommandSender sender, String note, SppPlayer target, boolean privateNote) {
        permissionHandler.validate(sender, privateNote ? permissionCreatePrivateNote : permissionCreateNote);

        if (StringUtils.isBlank(note)) {
            throw new BusinessException("Note cannot be empty");
        }

        PlayerNote playerNote = new PlayerNote(note, sender, target, privateNote, options.serverName);
        long id = playerNoteRepository.createPlayerNote(playerNote);
        playerNote.setId(id);

        sendEvent(new PlayerNoteCreatedEvent(playerNote, sender));
    }

    public List<PlayerNote> getAllPlayerNotes(CommandSender sender, UUID targetUuid, int offset, int amount) {
        UUID senderUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : Constants.CONSOLE_UUID;
        return playerNoteRepository.getPlayerNotesForTarget(senderUuid, targetUuid, offset, amount);
    }

    public void deleteNote(CommandSender staff, int noteId) {
        permissionHandler.validate(staff, permissionDelete);

        PlayerNote playerNote = playerNoteRepository.findNote(noteId).orElseThrow(() -> new BusinessException("&CNo note with id [" + noteId + "] found"));
        UUID senderUuid = staff instanceof Player ? ((Player) staff).getUniqueId() : Constants.CONSOLE_UUID;
        if (!playerNote.getNotedByUuid().equals(senderUuid)) {
            permissionHandler.validate(staff, permissionDeleteOther);
        }

        playerNoteRepository.deleteNote(noteId);
        sendEvent(new PlayerNoteDeletedEvent(playerNote, staff));
    }
}
