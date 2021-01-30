package net.shortninja.staffplus.staff.mute.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.mute.Mute;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.util.lib.JavaUtils.formatLines;

public class MutedPlayerItemBuilder implements InfractionGuiProvider<Mute> {

    public static ItemStack build(Mute mute) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(mute.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + mute.getId());
        lore.add("&bMuted player: " + mute.getPlayerName());
        lore.add("&bIssuer: " + mute.getIssuerName());
        lore.add("&bIssued on: " + time);
        lore.add("&bReason:");
        for (String line : formatLines(mute.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        if(mute.getEndTimestamp() != null) {
            lore.add("&bTime left:");
            lore.add("  &b" + mute.getHumanReadableDuration());
        }else {
            lore.add("&bPermanent mute");
        }
        ItemStack item = Items.builder()
            .setMaterial(Material.PLAYER_HEAD)
            .setName("&3Mute")
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(mute.getId()));
    }

    @Override
    public String getType() {
        return "MUTE";
    }

    @Override
    public ItemStack getMenuItem(Mute mute) {
        return build(mute);
    }
}
