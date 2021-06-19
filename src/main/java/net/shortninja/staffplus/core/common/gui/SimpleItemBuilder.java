package net.shortninja.staffplus.core.common.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.core.common.JavaUtils.formatLines;

public class SimpleItemBuilder {

    public static ItemStack build(String name, String loreString, Material material) {
        List<String> lore = new ArrayList<>();

        if(loreString != null) {
            for (String line : formatLines(loreString, 30)) {
                lore.add("&7" + line);
            }
        }

        ItemStack item = Items.editor(Items.builder().setMaterial(material).build())
            .setAmount(1)
            .setName("&6" + name)
            .setLore(lore)
            .build();

        return StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().addNbtString(item, name);
    }


}
