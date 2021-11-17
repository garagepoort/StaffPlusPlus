package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;

@IocBean
public class AppealReasonSelectViewBuilder {
    private static final int SIZE = 54;

    private final Options options;

    public AppealReasonSelectViewBuilder(Options options) {
        this.options = options;
    }

    public TubingGui buildGui(int warningId) {
        TubingGui.Builder builder = new TubingGui.Builder("Select appeal reason", SIZE);
        int count = 0;
        for (String appealReason : options.appealConfiguration.appealReasons) {
            builder.addItem(getAppealAction(warningId, appealReason), count, AppealReasonItemBuilder.build(appealReason));
            count++;
        }
        return builder.build();
    }

    private String getAppealAction(int warningId, String appealReason) {
        return GuiActionBuilder.builder()
            .action("manage-warning-appeals/create")
            .param("warningId", String.valueOf(warningId))
            .param("reason", appealReason)
            .build();
    }
}