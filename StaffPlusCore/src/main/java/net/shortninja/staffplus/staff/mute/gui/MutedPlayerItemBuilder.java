package net.shortninja.staffplus.staff.mute.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.mute.Mute;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MutedPlayerItemBuilder {

    public static ItemStack build(Mute mute) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + mute.getId());
        lore.add("&bMuted player: " + mute.getPlayerName());
        lore.add("&bIssuer: " + mute.getIssuerName());
        lore.add("&bReason: " + mute.getReason());
        if(mute.getEndTimestamp() != null) {
            lore.add("&bTime left: " + mute.getHumanReadableDuration());
        }else {
            lore.add("&bPermanent mute");
        }
        ItemStack item = Items.builder()
            .setMaterial(Material.BANNER)
            .setName(mute.getPlayerName())
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(mute.getId()));
    }

}
