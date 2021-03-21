package net.shortninja.staffplus.domain.staff.warn.appeals.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AppealReasonItemBuilder {

    public static ItemStack build(String reason) {
        List<String> lore = new ArrayList<>();

        ItemStack item = Items.editor(Items.builder().setMaterial(Material.PAPER).build())
            .setAmount(1)
            .setName("&b" + reason)
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, reason);
    }


}
