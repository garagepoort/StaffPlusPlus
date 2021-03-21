package net.shortninja.staffplus.domain.staff.examine.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SeverityLevelItemBuilder {

    public static ItemStack build(WarningSeverityConfiguration severityConfiguration) {
        List<String> lore = new ArrayList<>();

        lore.add("&bScore: " + severityConfiguration.getScore());
        ItemStack item = Items.editor(Items.builder().setMaterial(Material.PAPER).build())
            .setAmount(1)
            .setName("&b" + severityConfiguration.getName())
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, severityConfiguration.getName());
    }


}
