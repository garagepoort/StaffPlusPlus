package net.shortninja.staffplus.domain.staff.reporting.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.domain.staff.reporting.config.ReportTypeConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.common.JavaUtils.formatLines;

public class ReportTypeItemBuilder {

    public static ItemStack build(ReportTypeConfiguration reportTypeConfiguration) {
        List<String> lore = new ArrayList<>();

        if(reportTypeConfiguration.getLore() != null) {
            for (String line : formatLines(reportTypeConfiguration.getLore(), 30)) {
                lore.add("&7" + line);
            }
        }

        ItemStack item = Items.editor(Items.builder().setMaterial(reportTypeConfiguration.getMaterial()).build())
            .setAmount(1)
            .setName("&6" + reportTypeConfiguration.getType())
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, reportTypeConfiguration.getType());
    }


}
