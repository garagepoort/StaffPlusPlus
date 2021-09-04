package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.investigate.database.investigation.InvestigationsRepository;
import net.shortninja.staffplus.core.domain.staff.investigate.database.notes.InvestigationNotesRepository;
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
    private final BukkitUtils bukkitUtils;
    private final PermissionHandler permissionHandler;
    private final Options options;
    private final Messages messages;

    public InvestigationNoteService(InvestigationsRepository investigationsRepository, InvestigationNotesRepository investigationNotesRepository, BukkitUtils bukkitUtils, PermissionHandler permissionHandler, Options options, Messages messages) {
        this.investigationsRepository = investigationsRepository;
        this.investigationNotesRepository = investigationNotesRepository;
        this.bukkitUtils = bukkitUtils;
        this.permissionHandler = permissionHandler;
        this.options = options;
        this.messages = messages;
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
        permissionHandler.validate(noteTaker, options.investigationConfiguration.getAddNotePermission());
        if (StringUtils.isEmpty(note)) {
            throw new BusinessException("Note not cannot be empty");
        }
    }

    public void deleteNote(Player player, Investigation investigation, int id) {
        permissionHandler.validate(player, options.investigationConfiguration.getDeleteNotePermission());
        Optional<InvestigationNoteEntity> noteEntity = investigationNotesRepository.find(id);
        if (noteEntity.isPresent()) {
            if (!noteEntity.get().getNotedByUuid().equals(player.getUniqueId())) {
                permissionHandler.validate(player, options.investigationConfiguration.getDeleteNoteOthersPermission());
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
