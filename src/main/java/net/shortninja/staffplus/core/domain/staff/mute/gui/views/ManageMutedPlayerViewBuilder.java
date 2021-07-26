package net.shortninja.staffplus.core.domain.staff.mute.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import org.bukkit.inventory.ItemStack;

@IocBean
public class ManageMutedPlayerViewBuilder {
    private static final int SIZE = 54;

    private final MutedPlayerItemBuilder mutedPlayerItemBuilder;

    public ManageMutedPlayerViewBuilder(MutedPlayerItemBuilder mutedPlayerItemBuilder) {
        this.mutedPlayerItemBuilder = mutedPlayerItemBuilder;
    }


    public TubingGui buildGui(Mute mute, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Player: " + mute.getTargetName(), SIZE);

        builder.addItem(TubingGuiActions.NOOP, 13, mutedPlayerItemBuilder.build(mute));

        // TODO add evidence button
        // investigationGuiComponent.addEvidenceButton(this, 14, mute);

        ItemStack unmuteItem = Items.createRedColoredGlass("Unmute player", "Click to unmute this player");

        builder.addItem("manage-mutes/unmute?muteId=" + mute.getId(), 30, unmuteItem);
        builder.addItem("manage-mutes/unmute?muteId=" + mute.getId(), 31, unmuteItem);
        builder.addItem("manage-mutes/unmute?muteId=" + mute.getId(), 32, unmuteItem);
        builder.addItem("manage-mutes/unmute?muteId=" + mute.getId(), 39, unmuteItem);
        builder.addItem("manage-mutes/unmute?muteId=" + mute.getId(), 40, unmuteItem);
        builder.addItem("manage-mutes/unmute?muteId=" + mute.getId(), 41, unmuteItem);

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        return builder.build();
    }

}