package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.warn.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SeverityLevelItemBuilder {

    public static ItemStack build(WarningSeverityConfiguration severityConfiguration) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bScore: " + severityConfiguration.getScore());
        ItemStack item = Items.editor(Items.builder().setMaterial(Material.PAPER).build())
            .setAmount(1)
            .setName("&b" + severityConfiguration.getName())
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, severityConfiguration.getName());
    }


}
