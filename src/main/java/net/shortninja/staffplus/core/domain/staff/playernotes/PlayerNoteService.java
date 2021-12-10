package net.shortninja.staffplus.core.domain.staff.playernotes;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.playernotes.database.PlayerNoteRepository;
import net.shortninja.staffplusplus.playernotes.PlayerNoteCreatedEvent;
import net.shortninja.staffplusplus.playernotes.PlayerNoteDeletedEvent;
import net.shortninja.staffplusplus.playernotes.PlayerNoteFilters;
import net.shortninja.staffplusplus.session.SppInteractor;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class PlayerNoteService {

    private final PlayerNoteRepository playerNoteRepository;
    private final Options options;

    public PlayerNoteService(PlayerNoteRepository playerNoteRepository, Options options) {
        this.playerNoteRepository = playerNoteRepository;
        this.options = options;
    }

    public void createNote(SppInteractor sender, String note, SppPlayer target, boolean privateNote) {

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

    public List<PlayerNote> findPlayerNotes(CommandSender sender, PlayerNoteFilters playerNoteFilters, int offset, int amount) {
        UUID senderUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : Constants.CONSOLE_UUID;
        return playerNoteRepository.findPlayerNotes(senderUuid, playerNoteFilters, offset, amount);
    }

    public void deleteNote(SppPlayer staff, int noteId) {
        PlayerNote playerNote = getNote(noteId);

        if (playerNote.isPrivateNote() && !playerNote.getNotedByUuid().equals(staff.getId())) {
            throw new BusinessException("&CNo note with id [" + noteId + "] found");
        }

        playerNoteRepository.deleteNote(noteId);
        sendEvent(new PlayerNoteDeletedEvent(playerNote, staff));
    }

    public PlayerNote getNote(int noteId) {
        return playerNoteRepository.findNote(noteId).orElseThrow(() -> new BusinessException("&CNo note with id [" + noteId + "] found"));
    }
}
