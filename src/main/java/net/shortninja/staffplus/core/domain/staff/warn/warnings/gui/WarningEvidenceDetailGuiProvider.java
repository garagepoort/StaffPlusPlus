package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplusplus.investigate.evidence.EvidenceGuiClick;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(EvidenceGuiClick.class)
public class WarningEvidenceDetailGuiProvider implements EvidenceGuiClick {

    @Override
    public String getType() {
        return "WARNING";
    }

    @Override
    public String getAction(Player player, int id, String backAction) {
        return GuiActionBuilder.builder()
            .action("manage-warnings/view/detail")
            .param("warningId", String.valueOf(id))
            .param("backAction", backAction)
            .build();
    }
}
