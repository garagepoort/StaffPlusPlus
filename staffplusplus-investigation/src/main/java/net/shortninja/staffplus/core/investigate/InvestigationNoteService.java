package net.shortninja.staffplus.core.investigate;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.investigate.config.InvestigationConfiguration;
import net.shortninja.staffplus.core.investigate.database.investigation.InvestigationsRepository;
import net.shortninja.staffplus.core.investigate.database.notes.InvestigationNotesRepository;
import net.shortninja.staffplusplus.investigate.InvestigationNoteCreatedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationNoteDeletedEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplusplus.investigate.InvestigationStatus.OPEN;

@IocBean
public class InvestigationNoteService {

    private final InvestigationsRepository investigationsRepository;
    private final InvestigationNotesRepository investigationNotesRepository;
    private final PermissionHandler permissionHandler;
    private final Messages messages;
    private final InvestigationConfiguration investigationConfiguration;

    public InvestigationNoteService(InvestigationsRepository investigationsRepository,
                                    InvestigationNotesRepository investigationNotesRepository,
                                    PermissionHandler permissionHandler,
                                    Messages messages,
                                    InvestigationConfiguration investigationConfiguration) {
        this.investigationsRepository = investigationsRepository;
        this.investigationNotesRepository = investigationNotesRepository;
        this.permissionHandler = permissionHandler;
        this.messages = messages;
        this.investigationConfiguration = investigationConfiguration;
    }

    public void addNote(Player noteTaker, String note) {
        validateNoteCreation(noteTaker, note);

        Investigation investigation = investigationsRepository.getInvestigationForInvestigator(noteTaker.getUniqueId(), Collections.singletonList(OPEN))
            .orElseThrow(() -> new BusinessException("&CYou currently have no investigation running.", messages.prefixInvestigations));

        InvestigationNoteEntity investigationNoteEntity = new InvestigationNoteEntity(
            investigation.getId(),
            note,
            noteTaker.getUniqueId(),
            noteTaker.getName());

        investigationNotesRepository.addNote(investigationNoteEntity);
        sendEvent(new InvestigationNoteCreatedEvent(investigation, investigationNoteEntity));
    }

    public void addNote(Player noteTaker, Investigation investigation, String note) {
        validateNoteCreation(noteTaker, note);

        InvestigationNoteEntity investigationNoteEntity = new InvestigationNoteEntity(
            investigation.getId(),
            note,
            noteTaker.getUniqueId(),
            noteTaker.getName());

        investigationNotesRepository.addNote(investigationNoteEntity);
        sendEvent(new InvestigationNoteCreatedEvent(investigation, investigationNoteEntity));
    }

    private void validateNoteCreation(Player noteTaker, String note) {
        permissionHandler.validate(noteTaker, investigationConfiguration.getAddNotePermission());
        if (StringUtils.isEmpty(note)) {
            throw new BusinessException("Note not cannot be empty");
        }
    }

    public void deleteNote(Player player, Investigation investigation, int id) {
        permissionHandler.validate(player, investigationConfiguration.getDeleteNotePermission());
        Optional<InvestigationNoteEntity> noteEntity = investigationNotesRepository.find(id);
        if (noteEntity.isPresent()) {
            if (!noteEntity.get().getNotedByUuid().equals(player.getUniqueId())) {
                permissionHandler.validate(player, investigationConfiguration.getDeleteNoteOthersPermission());
            }
            investigationNotesRepository.removeNote(id);
            sendEvent(new InvestigationNoteDeletedEvent(investigation, noteEntity.get()));
        }
    }

    public List<InvestigationNoteEntity> getNotesForInvestigation(Investigation investigation) {
        return investigationNotesRepository.getAllNotes(investigation.getId());
    }

    public List<InvestigationNoteEntity> getNotesForInvestigation(Investigation investigation, int offset, int amount) {
        return investigationNotesRepository.getAllNotes(investigation.getId(), offset, amount);
    }
}
