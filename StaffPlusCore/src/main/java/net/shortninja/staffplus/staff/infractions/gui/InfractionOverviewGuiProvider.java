package net.shortninja.staffplus.staff.infractions.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.InfractionOverview;
import net.shortninja.staffplus.common.Items;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfractionOverviewGuiProvider {
    public static ItemStack build(InfractionOverview infractionOverview) {
        List<String> lore = new ArrayList<String>();
        lore.add("&bTotal: &6" + infractionOverview.getTotal());
        infractionOverview.getInfractions().forEach((type, count) -> {
            lore.add("&b" + type.getGuiTitle() + ": &6" + count);

        });

        ItemStack item = Items.editor(Items.createSkull(infractionOverview.getSppPlayer().getUsername())).setAmount(1)
            .setName(infractionOverview.getSppPlayer().getUsername())
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(infractionOverview.getSppPlayer().getId()));
    }
}
