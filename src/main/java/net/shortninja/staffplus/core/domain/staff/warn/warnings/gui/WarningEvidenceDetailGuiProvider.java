package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplusplus.investigate.evidence.EvidenceGuiClick;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(EvidenceGuiClick.class)
public class WarningEvidenceDetailGuiProvider implements EvidenceGuiClick {

    private final WarnService warnService;

    public WarningEvidenceDetailGuiProvider(WarnService muteService) {
        this.warnService = muteService;
    }

//    @Override
//    public void onClick(Player player, SppPlayer target, int id, Runnable back) {
//        Warning warning = warnService.getWarning(id);
//        ManageWarningGui manageWarningGui = new ManageWarningGui(player, "Manage warning", warning, null);
//        manageWarningGui.show(player);
//        manageWarningGui.setItem(49, Items.createBackDoor(), new IAction() {
//            @Override
//            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
//                back.run();
//            }
//
//            @Override
//            public boolean shouldClose(Player player) {
//                return false;
//            }
//        });
//    }

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
