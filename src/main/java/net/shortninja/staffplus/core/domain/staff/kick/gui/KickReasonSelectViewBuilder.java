package net.shortninja.staffplus.core.domain.staff.kick.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.SimpleItemBuilder;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickReasonConfiguration;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.getInventorySize;

@IocBean
public class KickReasonSelectViewBuilder {
    private final Options options;

    public KickReasonSelectViewBuilder(Options options) {
        this.options = options;
    }

    public TubingGui buildGui(String targetPlayerName) {
        TubingGui.Builder builder = new TubingGui.Builder("Select the reason for kick", getInventorySize(options.kickConfiguration.getKickReasons().size()));
        int count = 0;
        for (KickReasonConfiguration r : options.kickConfiguration.getKickReasons()) {
            String action = GuiActionBuilder.builder()
                .action("manage-kicks/kick")
                .param("targetPlayerName", targetPlayerName)
                .param("reason", r.getReason())
                .build();
            builder.addItem(action, count, SimpleItemBuilder.build(r.getReason(), r.getLore(), r.getMaterial()));
            count++;
        }
        return builder.build();
    }
}