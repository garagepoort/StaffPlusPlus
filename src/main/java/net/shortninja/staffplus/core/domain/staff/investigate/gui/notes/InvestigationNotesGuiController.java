package net.shortninja.staffplus.core.domain.staff.investigate.gui.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteEntity;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteService;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class InvestigationNotesGuiController {

    private static final String CANCEL = "cancel";
    private static final int PAGE_SIZE = 45;

    private final InvestigationService investigationService;
    private final InvestigationNoteService investigationNoteService;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;

    public InvestigationNotesGuiController(InvestigationService investigationService,
                                           InvestigationNoteService investigationNoteService,
                                           Messages messages,
                                           OnlineSessionsManager sessionManager,
                                           BukkitUtils bukkitUtils) {
        this.investigationService = investigationService;
        this.investigationNoteService = investigationNoteService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
    }

    @GuiAction("manage-investigation-notes/view")
    public AsyncGui<GuiTemplate> getNotesOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                                  @GuiParam("investigationId") int investigationId) {
        return async(() -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);
            List<InvestigationNoteEntity> notes = investigationNoteService.getNotesForInvestigation(investigation, page * PAGE_SIZE, PAGE_SIZE);
            HashMap<String, Object> params = new HashMap<>();
            params.put("notes", notes);
            return GuiTemplate.template("gui/investigations/notes-overview.ftl", params);
        });
    }

    @GuiAction("manage-investigation-notes/view/delete")
    public GuiTemplate getDetail(@GuiParam("noteId") int noteId, @GuiParam("investigationId") int investigationId, @GuiParam("backAction") String backAction) {
        String confirmAction = GuiActionBuilder.builder()
            .action("manage-investigation-notes/delete")
            .param("noteId", String.valueOf(noteId))
            .param("investigationId", String.valueOf(investigationId))
            .param("backAction", backAction)
            .build();

        HashMap<String, Object> params = new HashMap<>();
        params.put("confirmationMessage", "Are you sure you want to delete note(ID=" + noteId + ")");
        params.put("title", "Delete note?");
        params.put("confirmAction", confirmAction);
        params.put("cancelAction", backAction);
        return GuiTemplate.template("gui/commons/confirmation.ftl", params);
    }

    @GuiAction("manage-investigation-notes/delete")
    public AsyncGui<String> deleteNote(Player player, @GuiParam("noteId") int noteId, @GuiParam("investigationId") int investigationId, @GuiParam("backAction") String backAction) {
        return async(() -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);
            investigationNoteService.deleteNote(player, investigation, noteId);
            return backAction;
        });
    }

    @GuiAction("manage-investigation-notes/create")
    public void createNote(Player player, @GuiParam("investigationId") int investigationId) {
        bukkitUtils.runTaskAsync(player, () -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);

            messages.send(player, "&1===================================================", messages.prefixInvestigations);
            messages.send(player, "&6Type your note in chat", messages.prefixInvestigations);
            messages.send(player, "&6      Type \"cancel\" to cancel adding a note ", messages.prefixInvestigations);
            messages.send(player, "&1===================================================", messages.prefixInvestigations);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled your note", messages.prefixInvestigations);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> investigationNoteService.addNote(player, investigation, message));
            });
        });
    }
}
