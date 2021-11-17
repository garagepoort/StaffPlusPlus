package net.shortninja.staffplus.core.domain.staff.examine.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;

@IocBean
public class SeverityLevelSelectViewBuilder {
    private static final int SIZE = 54;

    private final Options options;

    public SeverityLevelSelectViewBuilder(Options options) {
        this.options = options;
    }

    public TubingGui buildGui(SppPlayer targetPlayer, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Select severity", SIZE);

        int slot = 0;
        for (WarningSeverityConfiguration severityConfiguration : options.warningConfiguration.getSeverityLevels()) {
            String createAction = GuiActionBuilder.builder()
                .action("manage-warnings/warn")
                .param("severity", severityConfiguration.getName())
                .param("targetPlayerName", targetPlayer.getUsername())
                .build();
            builder.addItem(createAction, slot, SeverityLevelItemBuilder.build(severityConfiguration));
            slot++;
        }

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        return builder.build();
    }
}