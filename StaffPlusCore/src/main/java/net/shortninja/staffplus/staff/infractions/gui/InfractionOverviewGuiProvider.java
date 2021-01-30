package net.shortninja.staffplus.staff.infractions.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.InfractionOverview;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfractionOverviewGuiProvider {
    public static ItemStack build(InfractionOverview infractionOverview) {
        List<String> lore = new ArrayList<String>();
        lore.add("&bTotal: &6" + infractionOverview.getTotal());
        infractionOverview.getInfractions().forEach((type, count) -> {
            lore.add("&b" + type + ": &6" + count);

        });
        ItemStack item = Items.builder()
            .setMaterial(Material.PLAYER_HEAD)
            .setName(infractionOverview.getSppPlayer().getUsername())
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(infractionOverview.getSppPlayer().getId()));
    }
}
