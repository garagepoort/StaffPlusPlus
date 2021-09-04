package net.shortninja.staffplus.core.domain.staff.investigate.database.notes;

import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteEntity;

import java.util.List;
import java.util.Optional;

public interface InvestigationNotesRepository {

    void addNote(InvestigationNoteEntity investigationNoteEntity);

    List<InvestigationNoteEntity> getAllNotes(int investigationId);

    List<InvestigationNoteEntity> getAllNotes(int investigationId, int offset, int amount);

    void removeNote(int id);

    Optional<InvestigationNoteEntity> find(int id);
}
