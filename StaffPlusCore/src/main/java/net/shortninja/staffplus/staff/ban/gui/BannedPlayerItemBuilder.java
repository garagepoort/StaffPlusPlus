package net.shortninja.staffplus.staff.ban.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.ban.Ban;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BannedPlayerItemBuilder {

    public static ItemStack build(Ban ban) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + ban.getId());
        lore.add("&bBanned player: " + ban.getPlayerName());
        lore.add("&bIssuer: " + ban.getIssuerName());
        lore.add("&bReason: " + ban.getReason());
        if(ban.getEndTimestamp() != null) {
            lore.add("&bTime left: " + ban.getHumanReadableDuration());
        }else {
            lore.add("&bPermanent ban");
        }
        ItemStack item = Items.builder()
            .setMaterial(Material.PLAYER_HEAD)
            .setName(ban.getPlayerName())
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(ban.getId()));
    }

}
