package net.shortninja.staffplus.core.domain.staff.investigate.database.notes;

import net.shortninja.staffplus.core.domain.staff.investigate.NoteEntity;

import java.util.List;
import java.util.Optional;

public interface InvestigationNotesRepository {

    void addNote(NoteEntity noteEntity);

    List<NoteEntity> getAllNotes(int investigationId);

    List<NoteEntity> getAllNotes(int investigationId, int offset, int amount);

    void removeNote(int id);

    Optional<NoteEntity> find(int id);
}
