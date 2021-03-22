package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ProtectedAreaItemBuilder {

    public static ItemStack build(ProtectedArea protectedArea) {
        List<String> lore = new ArrayList<>();

        lore.add("&bId: " + protectedArea.getId());
        lore.add("&bName: " + protectedArea.getName());
        lore.add("&bWorld: " + protectedArea.getCornerPoint1().getWorld().getName());
        lore.add(String.format("&bCorner point 1: X: %s Y: %s Z: %s",
            protectedArea.getCornerPoint1().getBlockX(),
            protectedArea.getCornerPoint1().getBlockY(),
            protectedArea.getCornerPoint1().getBlockZ()));
        lore.add(String.format("&bCorner point 2: X: %s Y: %s Z: %s",
            protectedArea.getCornerPoint2().getBlockX(),
            protectedArea.getCornerPoint2().getBlockY(),
            protectedArea.getCornerPoint2().getBlockZ()));

        ItemStack item = Items.builder()
            .setMaterial(Material.PAPER)
            .setName(protectedArea.getName())
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(protectedArea.getId()));
    }


}
