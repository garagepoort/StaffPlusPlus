package net.shortninja.staffplus.core.domain.staff.investigate.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteService;
import net.shortninja.staffplus.core.domain.staff.investigate.NoteEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

@IocBean
public class NoteOverviewViewBuilder {

    private static final int PAGE_SIZE = 45;
    private final InvestigationNoteService investigationNoteService;
    private final InvestigationNoteItemBuilder investigationEvidenceItemBuilder;

    public NoteOverviewViewBuilder(InvestigationNoteService investigationNoteService, InvestigationNoteItemBuilder investigationEvidenceItemBuilder) {
        this.investigationNoteService = investigationNoteService;
        this.investigationEvidenceItemBuilder = investigationEvidenceItemBuilder;
    }


    public TubingGui buildGui(Investigation investigation, int page, String backAction) {
        String backToOverviewAction = GuiActionBuilder.builder().action("manage-investigation-notes/view")
            .param("investigationId", String.valueOf(investigation.getId()))
            .param("page", String.valueOf(page))
            .param("backAction", backAction)
            .build();

        return new PagedGuiBuilder.Builder("Investigation Notes")
            .addPagedItems("manage-investigation-notes/view",
                getItems(investigation, page * PAGE_SIZE, PAGE_SIZE),
                investigationEvidenceItemBuilder::build,
                note -> TubingGuiActions.NOOP,
                getDeleteAction(backToOverviewAction),
                page, PAGE_SIZE)
            .backAction(backAction)
            .addItem(getCreateAction(investigation), 50, Items.createBook("Add note", ""))
            .build();
    }

    @NotNull
    private Function<NoteEntity, String> getDeleteAction(String backToOverviewAction) {
        return note -> GuiActionBuilder.builder()
            .action("manage-investigation-notes/view/delete")
            .param("noteId", String.valueOf(note.getId()))
            .param("investigationId", String.valueOf(note.getInvestigationId()))
            .param("backAction", backToOverviewAction)
            .build();
    }

    @NotNull
    private String getCreateAction(Investigation investigation) {
        return "manage-investigation-notes/create?investigationId=" + investigation.getId();
    }

    public List<NoteEntity> getItems(Investigation investigation, int offset, int amount) {
        return investigationNoteService.getNotesForInvestigation(investigation, offset, amount);
    }

}