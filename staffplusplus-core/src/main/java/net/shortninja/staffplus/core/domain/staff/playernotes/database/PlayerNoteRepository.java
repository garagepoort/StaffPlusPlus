package net.shortninja.staffplus.core.domain.staff.playernotes.database;

import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNote;
import net.shortninja.staffplusplus.playernotes.PlayerNoteFilters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerNoteRepository {

    long createPlayerNote(PlayerNote playerNote);

    List<PlayerNote> getPlayerNotesForTarget(UUID notedByUuid, UUID targetUuid, int offset, int amount);

    Optional<PlayerNote> findNote(int noteId);

    void deleteNote(int noteId);

    List<PlayerNote> findPlayerNotes(UUID notedByUuid, PlayerNoteFilters playerNoteFilters, int offset, int amount);
}
