package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import org.bukkit.inventory.ItemStack;

@IocBean
public class ManageBannedPlayerViewBuilder {
    private static final int SIZE = 54;

    private final BannedPlayerItemBuilder bannedPlayerItemBuilder;

    public ManageBannedPlayerViewBuilder(BannedPlayerItemBuilder bannedPlayerItemBuilder) {
        this.bannedPlayerItemBuilder = bannedPlayerItemBuilder;
    }

    public TubingGui buildGui(Ban ban, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Player: " + ban.getTargetName(), SIZE);

        builder.addItem(TubingGuiActions.NOOP, 13, bannedPlayerItemBuilder.build(ban));

        // TODO add evidence button
        // investigationGuiComponent.addEvidenceButton(this, 14, ban);

        ItemStack unbanItem = Items.createRedColoredGlass("Unban player", "Click to unban this player");

        builder.addItem("manage-bans/unban?banId=" + ban.getId(), 30, unbanItem);
        builder.addItem("manage-bans/unban?banId=" + ban.getId(), 31, unbanItem);
        builder.addItem("manage-bans/unban?banId=" + ban.getId(), 32, unbanItem);
        builder.addItem("manage-bans/unban?banId=" + ban.getId(), 39, unbanItem);
        builder.addItem("manage-bans/unban?banId=" + ban.getId(), 40, unbanItem);
        builder.addItem("manage-bans/unban?banId=" + ban.getId(), 41, unbanItem);

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        return builder.build();
    }

}