package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplusplus.investigate.evidence.EvidenceGuiClick;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(EvidenceGuiClick.class)
public class BanEvidenceDetailGuiProvider implements EvidenceGuiClick {

    private final BanService banService;

    public BanEvidenceDetailGuiProvider(BanService banService) {
        this.banService = banService;
    }

    @Override
    public void onClick(Player player, SppPlayer target, int id, Runnable back) {
        Ban ban = banService.getById(id);
        //TODO hook this up again
//        ManageBannedPlayerGui manageBannedPlayerGui = new ManageBannedPlayerGui("Player: " + ban.getTargetName(), ban, null);
//        manageBannedPlayerGui.show(player);
//        manageBannedPlayerGui.setItem(49, Items.createBackDoor(), new IAction() {
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
        return "BAN";
    }
}
