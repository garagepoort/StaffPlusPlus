package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplusplus.investigate.evidence.EvidenceGuiClick;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(EvidenceGuiClick.class)
public class MuteEvidenceDetailGuiProvider implements EvidenceGuiClick {

    private final MuteService muteService;

    public MuteEvidenceDetailGuiProvider(MuteService muteService) {
        this.muteService = muteService;
    }

    @Override
    public void onClick(Player player, SppPlayer target, int id, Runnable back) {
//        Mute mute = muteService.getById(id);
//        ManageMutedPlayerGui manageMutedPlayerGui = new ManageMutedPlayerGui("Player: " + mute.getTargetName(), mute, null);
//        manageMutedPlayerGui.show(player);
//        manageMutedPlayerGui.setItem(49, Items.createBackDoor(), new IAction() {
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
    }

    @Override
    public String getType() {
        return "MUTE";
    }
}
