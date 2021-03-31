package net.shortninja.staffplus.core.domain.staff.examine.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
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

        return StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().addNbtString(item, severityConfiguration.getName());
    }


}
